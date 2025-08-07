package com.eats.dto;

import com.eats.entity.DishFlavor;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDTO implements Serializable {

    private Long id;
    //料理�?
    private String name;
    //料理カテゴリID
    private Long categoryId;
    //料理価格
    private BigDecimal price;
    //画像
    private String image;
    //説明情報
    private String description;
    //0: 販売停止 1: 販売�?
    private Integer status;
    //フレーバ�?
    private List<DishFlavor> flavors = new ArrayList<>();

}
