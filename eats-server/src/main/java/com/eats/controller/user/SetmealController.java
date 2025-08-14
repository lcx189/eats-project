package com.eats.controller.user;

import com.eats.constant.StatusConstant;
import com.eats.entity.Setmeal;
import com.eats.result.Result;
import com.eats.service.SetmealService;
import com.eats.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C側セットメニュー閲覧API")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 条件検索
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("カテゴリIDに基づいてセットメニューを検索")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId") //key: setmealCache::100
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    /**
     * セットメニューIDに基づいて含まれる料理リストを検
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("セットメニューIDに基づいて含まれる料理リストを検索")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }
}
