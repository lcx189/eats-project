package com.eats.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 注文
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     * 注文ステータ1:支払い待2:受注待ち 3:受注済み 4:配達5:完了 6:キャンセル済
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer CONFIRMED = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED = 5;
    public static final Integer CANCELLED = 6;

    /**
     * 支払いステータス 0:未払1:支払い済2:返金
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    private static final long serialVersionUID = 1L;

    private Long id;

    //注文番号
    private String number;

    //注文ステータ1:支払い待2:受注待ち 3:受注済み 4:配達5:完了 6:キャンセル済7:返金
    private Integer status;

    //注文ユーザーID
    private Long userId;

    //住所ID
    private Long addressBookId;

    //注文時間
    private LocalDateTime orderTime;

    //会計時間
    private LocalDateTime checkoutTime;

    //支払い方1:WeChat 2:AliPay
    private Integer payMethod;

    //支払いステータス 0:未払1:支払い済2:返金
    private Integer payStatus;

    //受領金額
    private BigDecimal amount;

    //備
    private String remark;

    //ユーザー
    private String userName;

    //電話番号
    private String phone;

    //住所
    private String address;

    //受取
    private String consignee;

    //注文キャンセル理
    private String cancelReason;

    //注文拒否理由
    private String rejectionReason;

    //注文キャンセル時
    private LocalDateTime cancelTime;

    //お届け予定時
    private LocalDateTime estimatedDeliveryTime;

    //配送ステータス 1:すぐに配0:具体的な時間を選
    private Integer deliveryStatus;

    //お届け時
    private LocalDateTime deliveryTime;

    //梱包
    private int packAmount;

    //食器の数
    private int tablewareNumber;

    //食器の数ステータ1:食事量に応じて提0:具体的な数量を選
    private Integer tablewareStatus;
}
