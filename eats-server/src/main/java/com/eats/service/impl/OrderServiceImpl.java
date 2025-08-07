package com.eats.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.eats.constant.MessageConstant;
import com.eats.context.BaseContext;
import com.eats.dto.*;
import com.eats.entity.*;
import com.eats.exception.AddressBookBusinessException;
import com.eats.exception.OrderBusinessException;
import com.eats.exception.ShoppingCartBusinessException;
import com.eats.mapper.*;
import com.eats.result.PageResult;
import com.eats.service.OrderService;
import com.eats.utils.HttpClientUtil;
import com.eats.utils.WeChatPayUtil;
import com.eats.vo.OrderPaymentVO;
import com.eats.vo.OrderStatisticsVO;
import com.eats.vo.OrderSubmitVO;
import com.eats.vo.OrderVO;
import com.eats.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * ユーザー注文
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        //1. 各種の業務例外を処理（アドレス帳が空、ショッピングカートが空
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook == null){
            //業務例外をスロー
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //ユーザーの配送先住所が配送範囲外かどうかを確
        //checkOutOfRange(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());

        //現在のユーザーのショッピングカートデータを照
        Long userId = BaseContext.getCurrentId();

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if(shoppingCartList == null || shoppingCartList.size() == 0){
            //業務例外をスロー
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //2. 注文テーブル件のデータを挿入
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);

        orderMapper.insert(orders);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        //3. 注文詳細テーブルにn件のデータを挿入
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();//注文詳細
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());//現在の注文詳細に関連する注文IDを設
            orderDetailList.add(orderDetail);
        }

        orderDetailMapper.insertBatch(orderDetailList);

        //4. 現在のユーザーのショッピングカートデータをクリア
        shoppingCartMapper.deleteByUserId(userId);

        //5. VOをカプセル化して結果を返
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();

        return orderSubmitVO;
    }

    @Value("${eats.shop.address}")
    private String shopAddress;

    @Value("${eats.baidu.ak}")
    private String ak;

    /**
     * 顧客の配送先住所が配送範囲外かどうかを確
     * @param address
     */
    private void checkOutOfRange(String address) {
        Map map = new HashMap();
        map.put("address",shopAddress);
        map.put("output","json");
        map.put("ak",ak);

        //店舗の緯度経度座標を取得
        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("店舗住所の解析に失敗しました");
        }

        //データ解
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        String lat = location.getString("lat");
        String lng = location.getString("lng");
        //店舗の緯度経度座
        String shopLngLat = lat + "," + lng;

        map.put("address",address);
        //ユーザーの配送先住所の緯度経度座標を取得
        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        jsonObject = JSON.parseObject(userCoordinate);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("配送先住所の解析に失敗しました");
        }

        //データ解
        location = jsonObject.getJSONObject("result").getJSONObject("location");
        lat = location.getString("lat");
        lng = location.getString("lng");
        //ユーザーの配送先住所の緯度経度座
        String userLngLat = lat + "," + lng;

        map.put("origin",shopLngLat);
        map.put("destination",userLngLat);
        map.put("steps_info","0");

        //ルート計
        String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving", map);

        jsonObject = JSON.parseObject(json);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("配送ルートの計画に失敗しました");
        }

        //データ解
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");

        if(distance > 5000){
            //配送距離が5000メートルを超えていま
            throw new OrderBusinessException("配送範囲外です");
        }
    }

    /**
     * 注文支払
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 現在ログインしているユーザーID
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //WeChat Pay APIを呼び出し、前払い取引單を生成
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //事業者注文番
                new BigDecimal(0.01), //支払金額、単位：
                "Eats出前注", //商品説明
                user.getOpenid() //WeChatユーザーのopenid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("この注文はすでに支払われています");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支払い成功、注文ステータスを更
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 現在ログインしているユーザーID
        Long userId = BaseContext.getCurrentId();

        // 注文番号に基づいて現在のユーザーの注文を照会
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 注文IDに基づいて注文のステータス、支払方法、支払ステータス、会計日時を更新
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        //WebSocketを介してクライアントブラウザにメッセージをプッシtype orderId content
        Map map = new HashMap();
        map.put("type",1); // 1は新規注文通知は顧客からの督促
        map.put("orderId",ordersDB.getId());
        map.put("content","注文番号" + outTradeNo);

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }

    /**
     * ユーザー側注文のページング検
     *
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    public PageResult pageQuery4User(int pageNum, int pageSize, Integer status) {
        // ページングを設定
        PageHelper.startPage(pageNum, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // ページング条件検
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 注文詳細を照会し、OrderVOにカプセル化して応答
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 注文ID

                // 注文詳細を照
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    /**
     * 注文詳細を照
     *
     * @param id
     * @return
     */
    public OrderVO details(Long id) {
        // IDに基づいて注文を検索
        Orders orders = orderMapper.getById(id);

        // この注文に対応する料セットメニュー詳細を照会
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // この注文とその詳細をOrderVOにカプセル化して返す
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * ユーザーが注文をキャンセ
     *
     * @param id
     */
    public void userCancelById(Long id) throws Exception {
        // IDに基づいて注文を検索
        Orders ordersDB = orderMapper.getById(id);

        // 注文が存在するかどうかを検証
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //注文ステータ1:支払い待2:受注待ち 3:受注済み 4:配達5:完了 6:キャンセル済
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 注文が受注待ちの状態でキャンセルされた場合、返金が必要です
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //WeChat Pay返金APIを呼び出
            weChatPayUtil.refund(
                    ordersDB.getNumber(), //事業者注文番
                    ordersDB.getNumber(), //事業者返金番
                    new BigDecimal(0.01),//返金額、単位：
                    new BigDecimal(0.01));//元注文金

            //支払ステータスを「返金」に更新
            orders.setPayStatus(Orders.REFUND);
        }

        // 注文ステータス、キャンセル理由、キャンセル日時を更
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("ユーザーキャンセル");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * もう一度注
     *
     * @param id
     */
    public void repetition(Long id) {
        // 現在のユーザーIDを照
        Long userId = BaseContext.getCurrentId();

        // 注文IDに基づいて現在の注文詳細を照
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        // 注文詳細オブジェクトをショッピングカートオブジェクトに変
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            // 元の注文詳細の料理情報をショッピングカートオブジェクトに再コピー
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        // ショッピングカートオブジェクトをデータベースに一括追
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 注文検索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        // 一部の注文ステータスでは、追加で注文料理情報を返し、OrdersをOrderVOに変換する必要があります
        List<OrderVO> orderVOList = getOrderVOList(page);

        return new PageResult(page.getTotal(), orderVOList);
    }

    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        // 注文料理情報を返す必要があるため、カスタムOrderVOで応答結果を作成
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                // 共通フィールドをOrderVOにコピー
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderDishes = getOrderDishesStr(orders);

                // 注文料理情報をorderVOにカプセル化し、orderVOListに追
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    /**
     * 注文IDに基づいて料理情報文字列を取
     *
     * @param orders
     * @return
     */
    private String getOrderDishesStr(Orders orders) {
        // 注文料理詳細情報（注文内の料理と数量）を照会
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 各注文料理情報を文字列に連結（形式：宮保鶏丁*3；）
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        // この注文に対応するすべての料理情報を連結
        return String.join("", orderDishList);
    }

    /**
     * 各ステータスの注文件数統
     *
     * @return
     */
    public OrderStatisticsVO statistics() {
        // ステータスに基づいて、受注待ち、配達待ち、配達中の注文件数をそれぞれ照会
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);

        // 照会したデータをorderStatisticsVOにカプセル化して応答
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
    }

    /**
     * 受注
     *
     * @param ordersConfirmDTO
     */
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();

        orderMapper.update(orders);
    }

    /**
     * 受注拒否
     *
     * @param ordersRejectionDTO
     */
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        // IDに基づいて注文を検索
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());

        // 注文が存在し、かつステータス（受注待ち）の場合のみ受注拒否が可能
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //支払ステータ
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAID) {
            //ユーザーは支払い済みのため、返金が必要です
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("返金申請：{}", refund);
        }

        // 受注拒否には返金が必要、注文IDに基づいて注文ステータス、拒否理由、キャンセル日時を更
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * 店舗が注文をキャンセ
     *
     * @param ordersCancelDTO
     */
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
        // IDに基づいて注文を検索
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());

        //支払ステータ
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == 1) {
            //ユーザーは支払い済みのため、返金が必要です
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("返金申請：{}", refund);
        }

        // 管理側からの注文キャンセルには返金が必要、注文IDに基づいて注文ステータス、キャンセル理由、キャンセル日時を更
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 注文を配
     *
     * @param id
     */
    public void delivery(Long id) {
        // IDに基づいて注文を検索
        Orders ordersDB = orderMapper.getById(id);

        // 注文が存在し、かつステータスであることを検証
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 注文ステータスを更新、ステータスを配達中に変
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderMapper.update(orders);
    }

    /**
     * 注文を完
     *
     * @param id
     */
    public void complete(Long id) {
        // IDに基づいて注文を検索
        Orders ordersDB = orderMapper.getById(id);

        // 注文が存在し、かつステータスであることを検証
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 注文ステータスを更新、ステータスを完了に変更
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * 顧客からの督
     * @param id
     */
    public void reminder(Long id) {
        // IDに基づいて注文を検索
        Orders ordersDB = orderMapper.getById(id);

        // 注文が存在するかどうかを検証
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Map map = new HashMap();
        map.put("type",2); //1は新規注文通知は顧客からの督促
        map.put("orderId",id);
        map.put("content","注文番号" + ordersDB.getNumber());

        //WebSocketを介してクライアントブラウザにメッセージをプッシ
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }
}
