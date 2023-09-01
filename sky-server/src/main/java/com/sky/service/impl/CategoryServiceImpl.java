package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.CategoryEntity;
import com.sky.entity.DishEntity;
import com.sky.entity.SetmealEntity;
import com.sky.exception.IllegalDeleteException;
import com.sky.exception.InvalidFieldException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageBean;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类业务层
 * @author zxj
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private DishMapper dishMapper;

    @Resource
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     *
     * @param categoryDTO 分类信息
     */
    @Override
    @Transactional
    public void insert(CategoryDTO categoryDTO) {
        // 1、拷贝属性
        CategoryEntity categoryEntity = new CategoryEntity();
        BeanUtils.copyProperties(categoryDTO, categoryEntity);

        // 2、补全属性
        categoryEntity.setStatus(1);

        // 3、执行插入
        categoryMapper.insert(categoryEntity);
    }

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO 分类分页查询条件
     * @return PageBean
     */
    @Override
    public PageBean pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        // 1、解析参数
        int page = categoryPageQueryDTO.getPage();
        int pageSize = categoryPageQueryDTO.getPageSize();
        String name = categoryPageQueryDTO.getName();
        Integer type = categoryPageQueryDTO.getType();

        // 2、构造查询条件
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (type != null) {
            queryWrapper.eq("type", type);
        }

        // 3、构造分页条件
        Page<CategoryEntity> pageWrapper = new Page<>(page, pageSize);

        // 4、执行查询
        Page<CategoryEntity> entityPage = categoryMapper.selectPage(pageWrapper, queryWrapper);
        List<CategoryEntity> records = entityPage.getRecords();

        // 5、查询总记录数
        Long total = categoryMapper.selectCount(queryWrapper);

        // 6、构造返回结果
        return PageBean.builder()
                .total(total)
                .records(records)
                .build();
    }

    /**
     * 根据id删除分类
     *
     * @param id 分类id 1、id不存在 2、分类下有菜品或套餐
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        // 1、id不存在
        CategoryEntity entity = categoryMapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("分类不存在");
        }

        // 2、分类下有菜品或套餐 setmeal dish
        QueryWrapper<DishEntity> dishEntityQueryWrapper = new QueryWrapper<>();
        QueryWrapper<SetmealEntity> setmealEntityQueryWrapper = new QueryWrapper<>();
        dishEntityQueryWrapper.eq("category_id", id);
        setmealEntityQueryWrapper.eq("category_id", id);
        Long dishCount = dishMapper.selectCount(dishEntityQueryWrapper);
        Long setmealCount = setmealMapper.selectCount(setmealEntityQueryWrapper);
        if (dishCount > 0 || setmealCount > 0) {
            throw new IllegalDeleteException("分类下有菜品或套餐，不能删除");
        }

        // 2、执行删除
        categoryMapper.deleteById(id);
    }

    /**
     * 根据id查询分类
     *
     * @param categoryPageQueryDTO 分类类型
     * @return PageBean
     */
    @Override
    public PageBean selectByType(CategoryPageQueryDTO categoryPageQueryDTO) {
        int page = categoryPageQueryDTO.getPage();
        int pageSize = categoryPageQueryDTO.getPageSize();
        Integer type = categoryPageQueryDTO.getType();
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        Page<CategoryEntity> pageWrapper = new Page<>(page, pageSize);
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        Page<CategoryEntity> entityPage = categoryMapper.selectPage(pageWrapper, queryWrapper);
        List<CategoryEntity> records = entityPage.getRecords();
        Long total = categoryMapper.selectCount(queryWrapper);
        return PageBean.builder()
                .total(total)
                .records(records)
                .build();
    }

    /**
     * 更新分类状态
     *
     * @param status 状态 1 启用 2 禁用
     * @param id     分类id
     */
    @Override
    @Transactional
    public void updateStatus(Integer status, Long id) {
        // 1、构造更新对象
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(id);
        categoryEntity.setStatus(status);

        // 2、从本地线程中拿到id，设置更新人，更新数据库，交给MetaObjectHandler处理
        categoryMapper.updateById(categoryEntity);
    }

    /**
     * 更新分类
     *
     * @param categoryDTO 分类信息 1、分类名称不能为空 2、排序不能为空
     */
    @Override
    @Transactional
    public void update(CategoryDTO categoryDTO) {
        // 1、为了兼顾分页查询，因此没有在DTO中对字段进行校验，这里需要校验
        String name = categoryDTO.getName();
        Integer sort = categoryDTO.getSort();
        if (name == null || name.isEmpty()) {
            throw new InvalidFieldException("分类名称不能为空");
        }
        if (sort == null) {
            throw new InvalidFieldException("排序不能为空");
        }

        // 2、构造更新对象
        CategoryEntity categoryEntity = new CategoryEntity();
        BeanUtils.copyProperties(categoryDTO, categoryEntity);

        // 3、从本地线程中拿到id，设置更新人，更新数据库，交给MetaObjectHandler处理
        categoryMapper.updateById(categoryEntity);
    }
}
