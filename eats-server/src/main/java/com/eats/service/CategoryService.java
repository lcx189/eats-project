package com.eats.service;

import com.eats.dto.CategoryDTO;
import com.eats.dto.CategoryPageQueryDTO;
import com.eats.entity.Category;
import com.eats.result.PageResult;
import java.util.List;

public interface CategoryService {

    /**
     * カテゴリを追�?
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * ページング検�?
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * IDに基づいてカテゴリを削除
     * @param id
     */
    void deleteById(Long id);

    /**
     * カテゴリを編�?
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * カテゴリの有効化・無効化
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * タイプに基づいてカテゴリを検�?
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
