package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.DishEntity;
import com.sky.entity.SetmealEntity;
import com.sky.entity.ShoppingCartEntity;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zxj
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Resource
    private DishMapper dishMapper;

    @Resource
    private SetmealMapper setmealMapper;


    @Override
    public void insert(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCartEntity);
        shoppingCartEntity.setUserId(BaseContext.getCurrentId());

        // 根据商品的id（可能是菜品id，可能是套餐id，二者不可能同时出现）查询购物车中是否存在本次添加的商品
        LambdaQueryWrapper<ShoppingCartEntity> queryWrapper = new LambdaQueryWrapper<>();
        // 如果是菜品，还需要判断口味是否相同  放在wrapper构造的or()之前，防止被覆盖 为什么会被覆盖？？？
        if (shoppingCartDTO.getDishFlavor() != null) {
            queryWrapper.eq(ShoppingCartEntity::getDishFlavor, shoppingCartDTO.getDishFlavor());
        }
        queryWrapper.eq(ShoppingCartEntity::getUserId, shoppingCartEntity.getUserId())
                .eq(ShoppingCartEntity::getDishId, shoppingCartDTO.getDishId())
                .or()
                .eq(ShoppingCartEntity::getSetmealId, shoppingCartDTO.getSetmealId());

        queryWrapper.orderByDesc(ShoppingCartEntity::getCreateTime);
        List<ShoppingCartEntity> shoppingCartEntityList = shoppingCartMapper.selectList(queryWrapper);

        // 如果查出来有数据，就说明本次添加的商品已经存在购物车中，需要更新数量，执行更新操作
        if (shoppingCartEntityList != null && shoppingCartEntityList.size() == 1) {
            // 有数据 更新数量
            ShoppingCartEntity shoppingCart = shoppingCartEntityList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateById(shoppingCart);
        }

        // 如果没有数据，就说明本次添加的商品不存在购物车中，需要插入新的数据，执行插入操作
        if (shoppingCartEntityList == null || shoppingCartEntityList.isEmpty()) {
            // 没有数据 插入操作 数量就是1
            Long dishId = shoppingCartDTO.getDishId();

            // 因为菜品id和套餐id在业务逻辑上是互斥存在的，所以当一个有值时，另一个一定为null
            // 前端提交过来的数据对于存入数据库而言是残缺的，需要先去数据库中查询，将购物车中对应商品的数据补充完整
            if (dishId != null) {
                // 本次添加的是菜品
                DishEntity dishEntity = dishMapper.selectById(dishId);
                shoppingCartEntity.setName(dishEntity.getName());
                shoppingCartEntity.setImage(dishEntity.getImage());
                shoppingCartEntity.setAmount(dishEntity.getPrice());
            }

            if (dishId == null) {
                // 本次添加的是套餐
                SetmealEntity setmealEntity = setmealMapper.selectById(shoppingCartDTO.getSetmealId());
                shoppingCartEntity.setName(setmealEntity.getName());
                shoppingCartEntity.setImage(setmealEntity.getImage());
                shoppingCartEntity.setAmount(setmealEntity.getPrice());
            }

            shoppingCartEntity.setNumber(1);
            shoppingCartMapper.insert(shoppingCartEntity);
        }

    }

    @Override
    public List<ShoppingCartEntity> showShoppingCart() {
        return new LambdaQueryChainWrapper<>(ShoppingCartEntity.class)
                .eq(ShoppingCartEntity::getUserId, BaseContext.getCurrentId())
                .list();
    }

    @Override
    public void cleanShoppingCart() {
        shoppingCartMapper.delete(new LambdaQueryWrapper<>(ShoppingCartEntity.class).eq(ShoppingCartEntity::getUserId, BaseContext.getCurrentId()));
    }
}
