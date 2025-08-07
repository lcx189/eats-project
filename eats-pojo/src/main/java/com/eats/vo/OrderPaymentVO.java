package com.eats.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentVO implements Serializable {
    private String nonceStr; //ランダム文字列
    private String paySign; //署名
    private String timeStamp; //タイムスタン�?
    private String signType; //署名アルゴリズム
    private String packageStr; //統一下單APIから返されるprepay_idパラメータ�?
}
