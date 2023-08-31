package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.CategoryEntity;
import com.sky.result.PageBean;

import java.util.List;

public interface CategoryService {

    void insert(CategoryDTO categoryDTO);

    PageBean pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void deleteById(Long id);

    List<CategoryEntity> selectByType(Integer type);

    void updateStatus(Integer status,Long id);

    void update(CategoryDTO categoryDTO);
}
