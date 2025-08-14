package com.eats.controller.admin;

import com.eats.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店舗関連API")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 店舗の営業状態を設定
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("店舗の営業状態を設定")
    public Result setStatus(@PathVariable Integer status){
        log.info("店舗の営業状態を「{}」に設定します",status == 1 ? "営業" : "閉店");
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }

    /**
     * 店舗の営業状態を取得
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("店舗の営業状態を取得")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        if (status == null) {
            status = 1; // デフォルト: 営業
            try { redisTemplate.opsForValue().setIfAbsent(KEY, status); } catch (Exception ignore) {}
        }
        log.info("取得した店舗の営業状態：{}", (status != null && status == 1) ? "営業" : "閉店");
        return Result.success(status);
    }
}
