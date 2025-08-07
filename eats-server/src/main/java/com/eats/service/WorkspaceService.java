package com.eats.service;

import com.eats.vo.BusinessDataVO;
import com.eats.vo.DishOverViewVO;
import com.eats.vo.OrderOverViewVO;
import com.eats.vo.SetmealOverViewVO;
import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * 期間に基づいて営業データを統
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 注文管理データを照会
     * @return
     */
    OrderOverViewVO getOrderOverView();

    /**
     * 料理概要を照
     * @return
     */
    DishOverViewVO getDishOverView();

    /**
     * セットメニュー概要を照会
     * @return
     */
    SetmealOverViewVO getSetmealOverView();

}
