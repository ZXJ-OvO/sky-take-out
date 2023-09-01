package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishEntity;
import com.sky.result.PageBean;

/**
 * @author zxj
 */
public interface DishService {

    void updateStatus(Integer status, Long id);

    PageBean pageQuery(DishPageQueryDTO dishPageQueryDTO);

    PageBean selectByCatId(DishPageQueryDTO dishPageQueryDTO);

    DishEntity selectById(Long id);

    void insert(DishDTO dishDTO);

    void batchDeleteByIds(String[] ids);

    void update(DishDTO dishDTO);
}
