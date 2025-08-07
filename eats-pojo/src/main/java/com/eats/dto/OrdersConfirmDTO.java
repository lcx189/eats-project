package com.eats.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersConfirmDTO implements Serializable {

    private Long id;
    //注文ステータ�?1:支払い待�?2:受注待ち 3:受注済み 4:配達�?5:完了 6:キャンセル済�?7:返金
    private Integer status;

}
