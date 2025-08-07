package com.eats.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PasswordEditDTO implements Serializable {

    //従業員ID
    private Long empId;

    //古いパスワー
    private String oldPassword;

    //新しいパスワード
    private String newPassword;

}
