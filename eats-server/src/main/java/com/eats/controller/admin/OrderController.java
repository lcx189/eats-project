package com.eats.controller.admin;

import com.eats.dto.OrdersCancelDTO;
import com.eats.dto.OrdersConfirmDTO;
import com.eats.dto.OrdersPageQueryDTO;
import com.eats.dto.OrdersRejectionDTO;
import com.eats.result.PageResult;
import com.eats.result.Result;
import com.eats.service.OrderService;
import com.eats.vo.OrderStatisticsVO;
import com.eats.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注文管理
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "注文管理API")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 注文検索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("注文検索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 各ステータスの注文件数統�?
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("各ステータスの注文件数統計")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 注文詳細
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("注文詳細を照会")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 受注
     *
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("受注")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 受注拒否
     *
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("受注拒否")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 注文をキャンセル
     *
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("注文をキャンセル")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 注文を配�?
     *
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("注文を配達")
    public Result delivery(@PathVariable("id") Long id) {
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 注文を完�?
     *
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("注文を完�?")
    public Result complete(@PathVariable("id") Long id) {
        orderService.complete(id);
        return Result.success();
    }
}
