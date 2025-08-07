package com.eats.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.eats.constant.MessageConstant;
import com.eats.constant.StatusConstant;
import com.eats.context.BaseContext;
import com.eats.dto.CategoryDTO;
import com.eats.dto.CategoryPageQueryDTO;
import com.eats.entity.Category;
import com.eats.exception.DeletionNotAllowedException;
import com.eats.mapper.CategoryMapper;
import com.eats.mapper.DishMapper;
import com.eats.mapper.SetmealMapper;
import com.eats.result.PageResult;
import com.eats.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * カテゴリビジネス
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * カテゴリを追
     * @param categoryDTO
     */
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        //プロパティをコピ
        BeanUtils.copyProperties(categoryDTO, category);

        //カテゴリのステータスはデフォルトで無0)です
        category.setStatus(StatusConstant.DISABLE);

        //作成日時、更新日時、作成者、更新者を設定
        //category.setCreateTime(LocalDateTime.now());
        //category.setUpdateTime(LocalDateTime.now());
        //category.setCreateUser(BaseContext.getCurrentId());
        //category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    /**
     * ページング検
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        //次のSQLでページングを行い、自動的にlimitキーワードを追加してページングします
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * IDに基づいてカテゴリを削除
     * @param id
     */
    public void deleteById(Long id) {
        //現在のカテゴリが料理に関連しているかどうかを照会し、関連している場合はビジネス例外をスローしま
        Integer count = dishMapper.countByCategoryId(id);
        if(count > 0){
            //現在のカテゴリには料理があるため、削除できません
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        //現在のカテゴリがセットメニューに関連しているかどうかを照会し、関連している場合はビジネス例外をスローしま
        count = setmealMapper.countByCategoryId(id);
        if(count > 0){
            //現在のカテゴリには料理があるため、削除できません
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        //カテゴリデータを削除
        categoryMapper.deleteById(id);
    }

    /**
     * カテゴリを編
     * @param categoryDTO
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        //更新日時、更新者を設定
        //category.setUpdateTime(LocalDateTime.now());
        //category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    /**
     * カテゴリの有効化・無効化
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                //.updateTime(LocalDateTime.now())
                //.updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    /**
     * タイプに基づいてカテゴリを検
     * @param type
     * @return
     */
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
