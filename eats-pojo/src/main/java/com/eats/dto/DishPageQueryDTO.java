package com.eats.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DishPageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private String name;

    //カテゴリID
    private Integer categoryId;

    //ステータ�?0: 無効 1: 有効
    private Integer status;

}
