package com.eats.dto;

import com.eats.entity.SetmealDish;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealDTO implements Serializable {

    private Long id;

    //カテゴリID
    private Long categoryId;

    //セットメニュー名
    private String name;

    //セットメニュー価
    private BigDecimal price;

    //ステータ0:無効 1:有効
    private Integer status;

    //説明情報
    private String description;

    //画像
    private String image;

    //セットメニューと料理の関
    private List<SetmealDish> setmealDishes = new ArrayList<>();

}
