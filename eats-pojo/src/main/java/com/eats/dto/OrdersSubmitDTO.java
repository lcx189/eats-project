package com.eats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersSubmitDTO implements Serializable {
    //アドレス帳ID
    private Long addressBookId;
    //支払い方
    private int payMethod;
    //備
    private String remark;
    //お届け予定時
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime;
    //配送ステータス 1:すぐに配0:具体的な時間を選
    private Integer deliveryStatus;
    //食器の数
    private Integer tablewareNumber;
    //食器の数ステータ1:食事量に応じて提0:具体的な数量を選
    private Integer tablewareStatus;
    //梱包
    private Integer packAmount;
    //合計金額
    private BigDecimal amount;
}
