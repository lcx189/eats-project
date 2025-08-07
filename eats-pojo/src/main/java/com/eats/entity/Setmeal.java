package com.eats.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * セットメニュ�?
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //カテゴリID
    private Long categoryId;

    //セットメニュー名
    private String name;

    //セットメニュー価�?
    private BigDecimal price;

    //ステータ�?0:無効 1:有効
    private Integer status;

    //説明情報
    private String description;

    //画像
    private String image;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
