package com.eats.controller.user;

import com.eats.constant.StatusConstant;
import com.eats.entity.Dish;
import com.eats.result.Result;
import com.eats.service.DishService;
import com.eats.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C�?料理閲覧API")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * カテゴリIDに基づいて料理を検索
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("カテゴリIDに基づいて料理を検索")
    public Result<List<DishVO>> list(Long categoryId) {

        //Redisのキーを構築、ルール：dish_カテゴリID
        String key = "dish_" + categoryId;

        //Redisに料理データが存在するかを照�?
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(list != null && list.size() > 0){
            //存在する場合、直接返し、データベースを照会する必要はありません
            return Result.success(list);
        }

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//販売中の料理を照�?

        //存在しない場合、データベースを照会し、照会したデータをRedisに格納します
        list = dishService.listWithFlavor(dish);
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }

}
