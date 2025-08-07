package com.eats.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ショッピングカー�?
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //名称
    private String name;

    //ユーザーID
    private Long userId;

    //料理ID
    private Long dishId;

    //セットメニューID
    private Long setmealId;

    //フレーバ�?
    private String dishFlavor;

    //数量
    private Integer number;

    //金額
    private BigDecimal amount;

    //画像
    private String image;

    private LocalDateTime createTime;
}
