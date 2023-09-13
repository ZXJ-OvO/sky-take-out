package com.sky.service.impl;


import com.sky.constant.RedisConstant;
import com.sky.service.ShopService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/09/13 09:16
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Integer getStatus() {
        return (Integer) redisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS);
    }

    @Override
    public void setStatus(Integer status) {
        redisTemplate.opsForValue().set(RedisConstant.SHOP_STATUS, status);
    }
}
