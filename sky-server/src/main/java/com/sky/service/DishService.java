package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishEntity;
import com.sky.result.PageBean;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @author zxj
 */
public interface DishService {

    /**
     * 菜品状态
     *
     * @param status 0停售 1起售
     * @param id     菜品id
     */
    void updateStatus(Integer status, Long id);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 分页查询条件
     * @return {@link PageBean}
     */
    PageBean pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     *
     * @param id 菜品id
     * @return {@link DishEntity}
     */
    DishVO selectById(Long id);

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品信息
     */
    void insertWithFlavor(DishDTO dishDTO);

    /**
     * 批量删除菜品
     *
     * @param ids 菜品id数组
     */
    void batchDeleteByIds(String[] ids);

    /**
     * 更新菜品
     *
     * @param dishDTO 菜品信息
     */
    void update(DishDTO dishDTO);

    /**
     * 根据菜品分类id查询菜品
     *
     * @param categoryId 菜品分类id
     * @return 菜品列表
     */
    List<DishEntity> listByCategoryId(Long categoryId);

    /**
     * 根据菜品分类id查询菜品
     *
     * @param categoryId 菜品/套餐分类id
     * @return 根据分类id查出来的分类下的所有菜品/套餐数据
     */
    List<DishVO> selectByCategoryId(Long categoryId);
}
