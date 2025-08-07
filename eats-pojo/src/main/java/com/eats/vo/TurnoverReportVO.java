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
public class TurnoverReportVO implements Serializable {

    //日付リスト（カンマ区切り）、例�?022-10-01,2022-10-02,2022-10-03
    private String dateList;

    //売上高リスト（カンマ区切り）、例�?06.0,1520.0,75.0
    private String turnoverList;

}
