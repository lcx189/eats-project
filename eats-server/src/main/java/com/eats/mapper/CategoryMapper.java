package com.eats.mapper;

import com.github.pagehelper.Page;
import com.eats.annotation.AutoFill;
import com.eats.enumeration.OperationType;
import com.eats.dto.CategoryPageQueryDTO;
import com.eats.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * データを挿入
     * @param category
     */
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);

    /**
     * ページング検
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * IDに基づいてカテゴリを削除
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    /**
     * IDに基づいてカテゴリを編集
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * タイプに基づいてカテゴリを検
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
