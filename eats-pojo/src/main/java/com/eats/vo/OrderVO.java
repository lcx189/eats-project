package com.eats.vo;

import com.eats.entity.OrderDetail;
import com.eats.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {

    //注文料理情報
    private String orderDishes;

    //注文詳細
    private List<OrderDetail> orderDetailList;

}
