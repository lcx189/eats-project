package com.eats.controller.admin;

import com.eats.dto.SetmealDTO;
import com.eats.dto.SetmealPageQueryDTO;
import com.eats.result.PageResult;
import com.eats.result.Result;
import com.eats.service.SetmealService;
import com.eats.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * セットメニュー管
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "セットメニュー関連API")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * セットメニューを追加
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("セットメニューを追加")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")//key: setmealCache::100
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * ページング検
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("ページング検索")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * セットメニューの一括削
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("セットメニューの一括削除")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * IDに基づいてセットメニューを検索し、編集ページのデータ表示に使
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("IDに基づいてセットメニューを検索")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * セットメニューを編集
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("セットメニューを編集")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * セットメニューの販売開始・停止
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("セットメニューの販売開始・停止")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id) {
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}
