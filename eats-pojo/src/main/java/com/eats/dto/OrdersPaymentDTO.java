package com.eats.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrdersPaymentDTO implements Serializable {
    //注文番号
    private String orderNumber;

    //支払い方�?
    private Integer payMethod;

}
