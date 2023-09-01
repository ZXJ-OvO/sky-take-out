package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishEntity;
import com.sky.result.PageBean;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zxj
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Override
    public void updateStatus(Integer status, Long id) {

    }

    @Override
    public PageBean pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        return null;
    }

    @Override
    public PageBean selectByCatId(DishPageQueryDTO dishPageQueryDTO) {
        return null;
    }

    @Override
    public DishEntity selectById(Long id) {
        return null;
    }

    @Override
    public void insert(DishDTO dishDTO) {

    }

    @Override
    public void batchDeleteByIds(String[] ids) {

    }

    @Override
    public void update(DishDTO dishDTO) {

    }
}
