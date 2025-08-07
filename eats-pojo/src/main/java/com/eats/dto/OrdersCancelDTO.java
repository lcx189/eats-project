package com.eats.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersCancelDTO implements Serializable {

    private Long id;
    //注文キャンセル理�?
    private String cancelReason;

}
