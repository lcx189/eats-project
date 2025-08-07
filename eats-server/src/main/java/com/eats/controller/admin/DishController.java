package com.eats.controller.admin;

import com.eats.dto.DishDTO;
import com.eats.dto.DishPageQueryDTO;
import com.eats.entity.Dish;
import com.eats.result.PageResult;
import com.eats.result.Result;
import com.eats.service.DishService;
import com.eats.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 料理管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "料理関連API")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 料理を追�?
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("料理を追�?")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("料理を追加：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        //キャッシュデータをクリア
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }

    /**
     * 料理のページング検索
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("料理のページング検索")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("料理のページング検索:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 料理の一括削�?
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("料理の一括削�?")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("料理の一括削除：{}", ids);
        dishService.deleteBatch(ids);

        //すべての料理キャッシュデータをクリアし、dish_で始まるすべてのキーを削除します
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * IDに基づいて料理を検索
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("IDに基づいて料理を検索")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("IDに基づいて料理を検索：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 料理を編�?
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("料理を編�?")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("料理を編集：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        //すべての料理キャッシュデータをクリアし、dish_で始まるすべてのキーを削除します
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 料理の販売開始・停止
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("料理の販売開始・停止")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);

        //すべての料理キャッシュデータをクリアし、dish_で始まるすべてのキーを削除します
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * カテゴリIDに基づいて料理を検索
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("カテゴリIDに基づいて料理を検索")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * キャッシュデータをクリア
     * @param pattern
     */
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
