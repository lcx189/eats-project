package com.eats.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "従業員がログイン時に渡すデータモデル")
public class EmployeeLoginDTO implements Serializable {

    @ApiModelProperty("ユーザー")
    private String username;

    @ApiModelProperty("パスワー")
    private String password;

}
