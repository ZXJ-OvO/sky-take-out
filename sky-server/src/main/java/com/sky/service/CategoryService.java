package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.CategoryEntity;
import com.sky.result.PageBean;

import java.util.List;

/**
 * @author zxj
 */
public interface CategoryService {
    /**
     * 新增分类
     *
     * @param categoryDTO 分类信息
     */
    void insert(CategoryDTO categoryDTO);

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO 分类分页查询条件
     * @return PageBean
     */
    PageBean pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据id删除分类
     *
     * @param id 分类id
     */
    void deleteById(Long id);

    /**
     * 根据id查询分类
     *
     * @param type 类型
     * @return List<CategoryEntity>
     */
    List<CategoryEntity> selectByType(Integer type);

    /**
     * 更新分类状态
     *
     * @param status 状态
     * @param id     分类id
     */
    void updateStatus(Integer status,Long id);

    /**
     * 更新分类
     *
     * @param categoryDTO 分类信息
     */
    void update(CategoryDTO categoryDTO);
}
