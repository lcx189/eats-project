package com.eats.service;

import com.eats.dto.SetmealDTO;
import com.eats.dto.SetmealPageQueryDTO;
import com.eats.entity.Setmeal;
import com.eats.result.PageResult;
import com.eats.vo.DishItemVO;
import com.eats.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * セットメニューを追加し、同時にセットメニューと料理の関連関係を保存する必要がありま
     *
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * ページング検
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * セットメニューの一括削
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * IDに基づいてセットメニューと関連する料理データを検
     *
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * セットメニューを編集
     *
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * セットメニューの販売開始・停
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 条件検索
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * IDに基づいて料理オプションを検
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
