package com.sky.service.impl;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.CategoryEntity;
import com.sky.result.PageBean;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类业务层
 * @author zxj
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Override
    public void insert(CategoryDTO categoryDTO) {

    }

    @Override
    public PageBean pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<CategoryEntity> selectByType(Integer type) {
        return null;
    }

    @Override
    public void updateStatus(Integer status, Long id) {

    }

    @Override
    public void update(CategoryDTO categoryDTO) {

    }
}
