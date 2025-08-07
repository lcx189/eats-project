package com.eats.service;

import com.eats.dto.DishDTO;
import com.eats.dto.DishPageQueryDTO;
import com.eats.entity.Dish;
import com.eats.result.PageResult;
import com.eats.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 料理と対応するフレーバーを追�?
     *
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 料理のページング検索
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 料理の一括削�?
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * IDに基づいて料理と対応するフレーバーデータを検�?
     *
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * IDに基づいて料理の基本情報と対応するフレーバー情報を編�?
     *
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 料理の販売開始・停止
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * カテゴリIDに基づいて料理を検索
     *
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);

    /**
     * 条件で料理とフレーバーを検索
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
