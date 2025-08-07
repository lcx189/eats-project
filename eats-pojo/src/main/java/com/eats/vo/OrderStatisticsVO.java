package com.eats.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderStatisticsVO implements Serializable {
    //受注待ち件数
    private Integer toBeConfirmed;

    //配達待ち件数
    private Integer confirmed;

    //配達中件�?
    private Integer deliveryInProgress;
}
