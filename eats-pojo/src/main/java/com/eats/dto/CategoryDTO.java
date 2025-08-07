package com.eats.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {

    //主キ�?
    private Long id;

    //タイ�?1: 料理カテゴリ 2: セットメニューカテゴ�?
    private Integer type;

    //カテゴリ�?
    private String name;

    //ソート順
    private Integer sort;

}
