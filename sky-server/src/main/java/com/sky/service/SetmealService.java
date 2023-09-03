package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageBean;
import com.sky.vo.SetmealVO;

public interface SetmealService {

    PageBean pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void insert(SetmealDTO setmealDTO);

    SetmealVO selectById(Long id);

    void update(SetmealDTO setmealDTO);
}
