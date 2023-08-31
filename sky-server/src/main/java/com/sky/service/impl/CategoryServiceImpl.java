package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.CategoryEntity;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageBean;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

    @Override
    public void insert(CategoryDTO categoryDTO) {
        // 1、拷贝属性
        CategoryEntity categoryEntity = new CategoryEntity();
        BeanUtils.copyProperties(categoryDTO, categoryEntity);

        // 2、补全属性
        categoryEntity.setCreateUser(BaseContext.getCurrentId());
        categoryEntity.setUpdateUser(BaseContext.getCurrentId());
        categoryEntity.setStatus(1);

        // 3、执行插入
        categoryMapper.insert(categoryEntity);
    }

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
