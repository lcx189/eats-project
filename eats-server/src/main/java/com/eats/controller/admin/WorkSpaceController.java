package com.eats.controller.admin;

import com.eats.result.Result;
import com.eats.service.WorkspaceService;
import com.eats.vo.BusinessDataVO;
import com.eats.vo.DishOverViewVO;
import com.eats.vo.OrderOverViewVO;
import com.eats.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * ワークスペー�?
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "ワークスペース関連API")
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * ワークスペースの本日データ照�?
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("ワークスペースの本日データ照�?")
    public Result<BusinessDataVO> businessData(){
        //当日の開始時間を取得
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //当日の終了時間を取得
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

    /**
     * 注文管理データを照会
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("注文管理データを照会")
    public Result<OrderOverViewVO> orderOverView(){
        return Result.success(workspaceService.getOrderOverView());
    }

    /**
     * 料理概要を照�?
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("料理概要を照�?")
    public Result<DishOverViewVO> dishOverView(){
        return Result.success(workspaceService.getDishOverView());
    }

    /**
     * セットメニュー概要を照会
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("セットメニュー概要を照会")
    public Result<SetmealOverViewVO> setmealOverView(){
        return Result.success(workspaceService.getSetmealOverView());
    }
}
