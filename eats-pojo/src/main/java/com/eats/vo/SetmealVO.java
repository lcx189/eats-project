package com.eats.vo;

import com.eats.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealVO implements Serializable {

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

    //更新時間
    private LocalDateTime updateTime;

    //カテゴリ�?
    private String categoryName;

    //セットメニューと料理の関連関�?
    private List<SetmealDish> setmealDishes = new ArrayList<>();
}
