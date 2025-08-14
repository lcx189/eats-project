package com.eats.controller.admin;

import com.eats.dto.CategoryDTO;
import com.eats.dto.CategoryPageQueryDTO;
import com.eats.entity.Category;
import com.eats.result.PageResult;
import com.eats.result.Result;
import com.eats.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * カテゴリ管理
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "カテゴリ関連API")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * カテゴリを追
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("カテゴリを追加")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("カテゴリを追加：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * カテゴリのページング検索
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("カテゴリのページング検索")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("ページング検索：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * カテゴリを削
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("カテゴリを削除")
    public Result<String> deleteById(Long id){
        log.info("カテゴリを削除：{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * カテゴリを編集
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("カテゴリを編集")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * カテゴリの有効化・無効化
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("カテゴリの有効化・無効化")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * タイプに基づいてカテゴリを検索
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("タイプに基づいてカテゴリを検索")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
