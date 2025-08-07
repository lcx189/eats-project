package com.eats.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishItemVO implements Serializable {

    //料理�?
    private String name;

    //数量
    private Integer copies;

    //料理画像
    private String image;

    //料理説明
    private String description;
}
