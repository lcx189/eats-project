package com.eats.controller.user;

import com.eats.dto.ShoppingCartDTO;
import com.eats.entity.ShoppingCart;
import com.eats.result.Result;
import com.eats.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C側ショッピングカート関連API")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * ショッピングカートに追加
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("ショッピングカートに追加")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("ショッピングカートに追加、商品情報：{}",shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * ショッピングカートを見る
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("ショッピングカートを見る")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }

    /**
     * ショッピングカートを空にする
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("ショッピングカートを空にする")
    public Result clean(){
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * ショッピングカートから商品を一つ削
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation("ショッピングカートから商品を一つ削除")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("ショッピングカートから商品を一つ削除、商品：{}", shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
