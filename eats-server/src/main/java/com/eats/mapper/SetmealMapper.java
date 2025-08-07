package com.eats.mapper;

import com.github.pagehelper.Page;
import com.eats.annotation.AutoFill;
import com.eats.dto.SetmealPageQueryDTO;
import com.eats.entity.Setmeal;
import com.eats.enumeration.OperationType;
import com.eats.vo.DishItemVO;
import com.eats.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * カテゴリIDに基づいてセットメニューの数を検�?
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * IDに基づいてセットメニューを編�?
     *
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * セットメニューを追加
     *
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * ページング検�?
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * IDに基づいてセットメニューを検�?
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * IDに基づいてセットメニューを削�?
     * @param setmealId
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long setmealId);

    /**
     * IDに基づいてセットメニューとセットメニューの料理関係を検索
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 動的条件でセットメニューを検�?
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * セットメニューIDに基づいて料理オプションを検�?
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 条件に基づいてセットメニュー数を統計
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
