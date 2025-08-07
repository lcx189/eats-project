package com.eats.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C側ユーザーログイ
 */
@Data
public class UserLoginDTO implements Serializable {

    private String code;

}
