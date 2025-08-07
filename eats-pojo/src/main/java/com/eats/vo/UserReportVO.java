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
public class UserReportVO implements Serializable {

    //日付リスト（カンマ区切り）、例�?022-10-01,2022-10-02,2022-10-03
    private String dateList;

    //総ユーザー数リスト（カンマ区切り）、例�?00,210,220
    private String totalUserList;

    //新規ユーザー数リスト（カンマ区切り）、例�?0,21,10
    private String newUserList;

}
