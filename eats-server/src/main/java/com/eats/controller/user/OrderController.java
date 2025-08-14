package com.eats.controller.user;

import com.eats.dto.OrdersPaymentDTO;
import com.eats.dto.OrdersSubmitDTO;
import com.eats.result.PageResult;
import com.eats.result.Result;
import com.eats.service.OrderService;
import com.eats.vo.OrderPaymentVO;
import com.eats.vo.OrderSubmitVO;
import com.eats.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "ユーザー側注文関連API")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * ユーザー注文
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("ユーザー注文")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("ユーザー注文、パラメータ：{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 注文支払
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("注文支払")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("注文支払い：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("前払い取引單を生成：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 過去の注文を照会
     *
     * @param page
     * @param pageSize
     * @param status   注文ステータ1:支払い待2:受注待ち 3:受注済み 4:配達5:完了 6:キャンセル済
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("過去の注文を照会")
    public Result<PageResult> page(int page, int pageSize, Integer status) {
        PageResult pageResult = orderService.pageQuery4User(page, pageSize, status);
        return Result.success(pageResult);
    }

    /**
     * 注文詳細を照
     *
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("注文詳細を照会")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * ユーザーが注文をキャンセ
     *
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("注文をキャンセル")
    public Result cancel(@PathVariable("id") Long id) throws Exception {
        orderService.userCancelById(id);
        return Result.success();
    }

    /**
     * もう一度注
     *
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("もう一度注文")
    public Result repetition(@PathVariable Long id) {
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * 顧客からの督促
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("顧客からの督促")
    public Result reminder(@PathVariable("id") Long id){
        orderService.reminder(id);
        return Result.success();
    }
}
