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
public class SalesTop10ReportVO implements Serializable {

    //商品名リスト（カンマ区切り）、例：魚香肉�?宮保鶏丁,水煮�?
    private String nameList;

    //販売数リスト（カンマ区切り）、例�?60,215,200
    private String numberList;

}
