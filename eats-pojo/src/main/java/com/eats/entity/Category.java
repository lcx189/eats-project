package com.eats.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //タイ�? 1:料理カテゴリ 2:セットメニューカテゴ�?
    private Integer type;

    //カテゴリ�?
    private String name;

    //順序
    private Integer sort;

    //カテゴリステータ�?0:無効 1:有効
    private Integer status;

    //作成時間
    private LocalDateTime createTime;

    //更新時間
    private LocalDateTime updateTime;

    //作成�?
    private Long createUser;

    //更新�?
    private Long updateUser;
}
