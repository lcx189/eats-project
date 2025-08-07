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
public class OrderReportVO implements Serializable {

    //日付リスト（カンマ区切り）、例022-10-01,2022-10-02,2022-10-03
    private String dateList;

    //日次注文件数リスト（カンマ区切り）、例60,210,215
    private String orderCountList;

    //日次有効注文件数リスト（カンマ区切り）、例0,21,10
    private String validOrderCountList;

    //総注文件
    private Integer totalOrderCount;

    //有効注文件数
    private Integer validOrderCount;

    //注文完了
    private Double orderCompletionRate;

}
