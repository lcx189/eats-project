package com.eats.service;

import com.eats.dto.*;
import com.eats.result.PageResult;
import com.eats.vo.OrderPaymentVO;
import com.eats.vo.OrderStatisticsVO;
import com.eats.vo.OrderSubmitVO;
import com.eats.vo.OrderVO;

public interface OrderService {
    /**
     * ユーザー注文
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 注文支払�?
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支払い成功、注文ステータスを更�?
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * ユーザー側注文のページング検�?
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQuery4User(int page, int pageSize, Integer status);

    /**
     * 注文詳細を照�?
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * ユーザーが注文をキャンセ�?
     * @param id
     */
    void userCancelById(Long id) throws Exception;

    /**
     * もう一度注�?
     * @param id
     */
    void repetition(Long id);

    /**
     * 条件で注文を検索
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各ステータスの注文件数統�?
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 受注
     *
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 受注拒否
     *
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 店舗が注文をキャンセ�?
     *
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 注文を配�?
     *
     * @param id
     */
    void delivery(Long id);

    /**
     * 注文を完�?
     *
     * @param id
     */
    void complete(Long id);

    /**
     * 顧客からの督�?
     * @param id
     */
    void reminder(Long id);
}
