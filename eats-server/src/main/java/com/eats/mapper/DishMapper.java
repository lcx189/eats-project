package com.eats.mapper;

import com.github.pagehelper.Page;
import com.eats.annotation.AutoFill;
import com.eats.dto.DishPageQueryDTO;
import com.eats.entity.Dish;
import com.eats.enumeration.OperationType;
import com.eats.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * カテゴリIDに基づいて料理数を検�?
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 料理データを挿入
     *
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 料理のページング検索
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 主キーに基づいて料理を検�?
     *
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 主キーに基づいて料理データを削除
     *
     * @param id
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * IDに基づいて料理データを動的に編集
     *
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 動的条件で料理を検索
     *
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * セットメニューIDに基づいて料理を検索
     * @param setmealId
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * 条件に基づいて料理数を統�?
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
