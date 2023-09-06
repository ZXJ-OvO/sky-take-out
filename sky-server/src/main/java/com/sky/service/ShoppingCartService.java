package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCartEntity;

import java.util.List;

/**
 * @author zxj
 */
public interface ShoppingCartService {

    void insert(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCartEntity> showShoppingCart();

}
