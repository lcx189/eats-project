package com.eats.dto;

import com.eats.entity.OrderDetail;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdersDTO implements Serializable {

    private Long id;

    //注文番号
    private String number;

    //注文ステータ�?1:支払い待�?2:配達待ち 3:配達済み 4:完了 5:キャンセル済�?
    private Integer status;

    //注文ユーザーID
    private Long userId;

    //住所ID
    private Long addressBookId;

    //注文時間
    private LocalDateTime orderTime;

    //会計時間
    private LocalDateTime checkoutTime;

    //支払い方�?1:WeChat 2:AliPay
    private Integer payMethod;

    //受領金額
    private BigDecimal amount;

    //備�?
    private String remark;

    //ユーザー�?
    private String userName;

    //電話番号
    private String phone;

    //住所
    private String address;

    //受取�?
    private String consignee;

    private List<OrderDetail> orderDetails;

}
