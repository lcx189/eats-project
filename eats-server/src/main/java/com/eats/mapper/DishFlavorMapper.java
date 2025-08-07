package com.eats.mapper;

import com.eats.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * フレーバーデータを一括挿
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 料理IDに基づいて対応するフレーバーデータを削除
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 料理IDに基づいて対応するフレーバーデータを検索
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
