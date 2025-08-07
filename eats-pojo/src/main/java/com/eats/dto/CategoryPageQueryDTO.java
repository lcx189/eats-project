package com.eats.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryPageQueryDTO implements Serializable {

    //ページ番
    private int page;

    //1ページあたりのレコード数
    private int pageSize;

    //カテゴリ
    private String name;

    //カテゴリタイ1: 料理カテゴリ 2: セットメニューカテゴ
    private Integer type;

}
