package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDishEntity;
import com.sky.entity.SetmealEntity;
import com.sky.result.PageBean;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    PageBean pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void insert(SetmealDTO setmealDTO);

    SetmealVO selectById(Long id);

    void update(SetmealDTO setmealDTO);

    void delete(String ids);

    void updateStatus(Integer status, Long id);

    List<SetmealEntity> selectByCategoryId(Long categoryId);

    List<DishItemVO> selectSetmealDishes(Long id);
}
