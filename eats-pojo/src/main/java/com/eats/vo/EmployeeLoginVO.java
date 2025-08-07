package com.eats.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "従業員ログイン時に返されるデータ形式")
public class EmployeeLoginVO implements Serializable {

    @ApiModelProperty("主キー")
    private Long id;

    @ApiModelProperty("ユーザー")
    private String userName;

    @ApiModelProperty("氏名")
    private String name;

    @ApiModelProperty("JWTトークン")
    private String token;

}
