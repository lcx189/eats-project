package com.eats.service;

import com.eats.dto.ShoppingCartDTO;
import com.eats.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * ショッピングカートに追加
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * ショッピングカートを見る
     * @return
     */
    List<ShoppingCart> showShoppingCart();

    /**
     * ショッピングカートを空にする
     */
    void cleanShoppingCart();

    /**
     * ショッピングカートから商品を一つ削
     * @param shoppingCartDTO
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
