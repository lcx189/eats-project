package com.eats.service.impl;

import com.eats.context.BaseContext;
import com.eats.dto.ShoppingCartDTO;
import com.eats.entity.Dish;
import com.eats.entity.Setmeal;
import com.eats.entity.ShoppingCart;
import com.eats.mapper.DishMapper;
import com.eats.mapper.SetmealMapper;
import com.eats.mapper.ShoppingCartMapper;
import com.eats.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.beancontext.BeanContext;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * ショッピングカートに追加
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //現在ショッピングカートに追加された商品がすでに存在するかどうかを判断
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //すでに存在する場合、数量を1増やすだけで�?
        if(list != null && list.size() > 0){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);//update shopping_cart set number = ? where id = ?
            shoppingCartMapper.updateNumberById(cart);
        }else {
            //存在しない場合、ショッピングカートデータを1件挿入する必要があります
            //今回ショッピングカートに追加されたのが料理かセットメニューかを判�?
            Long dishId = shoppingCartDTO.getDishId();
            if(dishId != null){
                //今回ショッピングカートに追加されたのは料理で�?
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else{
                //今回ショッピングカートに追加されたのはセットメニューです
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * ショッピングカートを見る
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        //現在のWeChatユーザーIDを取�?
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * ショッピングカートを空にする
     */
    public void cleanShoppingCart() {
        //現在のWeChatユーザーIDを取�?
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * ショッピングカートから商品を一つ削�?
     * @param shoppingCartDTO
     */
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //検索条件を設定し、現在ログインしているユーザーのショッピングカートデータを照�?
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if(list != null && list.size() > 0){
            shoppingCart = list.get(0);

            Integer number = shoppingCart.getNumber();
            if(number == 1){
                //現在の商品のショッピングカートでの数量が1の場合、現在のレコードを直接削�?
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else {
                //現在の商品のショッピングカートでの数量が1でない場合、数量を編集するだけです
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }
}
