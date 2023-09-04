package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.support.result.WordResultHandlers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.CategoryEntity;
import com.sky.entity.SetmealDishEntity;
import com.sky.entity.SetmealEntity;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageBean;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Resource
    private SetmealMapper setmealMapper;

    @Resource
    private SetmealDishMapper setmealDishMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public PageBean pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> pageQuery = setmealMapper.pageQuery(setmealPageQueryDTO);

        List<SetmealVO> result = pageQuery.getResult();
        long total = pageQuery.getTotal();

        return PageBean.builder()
                .total(total)
                .records(result)
                .build();
    }

    @Override
    @Transactional
    public void insert(SetmealDTO setmealDTO) {
        // 从setmealDTO中获取菜品集合数据
        List<SetmealDishEntity> setmealDishEntities = setmealDTO.getSetmealDishes();
        log.info("setmealDishEntities:{}", setmealDishEntities);
        // 保存套餐主体信息
        SetmealEntity setmealEntity = new SetmealEntity();
        BeanUtils.copyProperties(setmealDTO, setmealEntity);


        // 信息过滤
        List<String> wordList = SensitiveWordHelper.findAll(setmealEntity.getDescription(), WordResultHandlers.word());
        log.info("检测到敏感词:{}", wordList);
        String replace = SensitiveWordHelper.replace(setmealEntity.getDescription());
        log.info("替换后的描述:{}", replace);
        setmealEntity.setDescription(replace);
        setmealMapper.insert(setmealEntity);


        // 保存套餐菜品关系信息
        for (SetmealDishEntity setmealDishEntity : setmealDishEntities) {
            setmealDishEntity.setSetmealId(setmealEntity.getId());
            setmealDishEntity.setDishId(setmealDishEntity.getDishId());
            setmealDishEntity.setCopies(setmealDishEntity.getCopies());
            setmealDishEntity.setPrice(setmealDishEntity.getPrice());
            setmealDishEntity.setName(setmealDishEntity.getName());
            log.info("setmealDishEntity:{}", setmealDishEntity);
            setmealDishMapper.insert(setmealDishEntity);
        }
    }

    @Override
    public SetmealVO selectById(Long id) {
        SetmealEntity entity = setmealMapper.selectById(id);
        SetmealVO vo = new SetmealVO();
        BeanUtils.copyProperties(entity, vo);

        Long categoryId = entity.getCategoryId();
        CategoryEntity categoryEntity = categoryMapper.selectById(categoryId);
        String name = categoryEntity.getName();
        vo.setCategoryName(name);

        QueryWrapper<SetmealDishEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id", id);
        List<SetmealDishEntity> dishEntities = setmealDishMapper.selectList(queryWrapper);
        vo.setSetmealDishes(dishEntities);

        log.info("vo:{}", vo);
        return vo;
    }

    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        SetmealEntity setmealEntity = new SetmealEntity();
        BeanUtils.copyProperties(setmealDTO, setmealEntity);
        List<SetmealDishEntity> setmealDishes = setmealDTO.getSetmealDishes();

        // 信息过滤
        List<String> wordList = SensitiveWordHelper.findAll(setmealEntity.getDescription(), WordResultHandlers.word());
        log.info("检测到敏感词:{}", wordList);
        String replace = SensitiveWordHelper.replace(setmealEntity.getDescription());
        log.info("替换后的描述:{}", replace);
        setmealEntity.setDescription(replace);

        log.info("setmealDishes:{}", setmealDishes);
        // 更新套餐主体信息
        setmealMapper.updateById(setmealEntity);

        // 更新套餐菜品关系信息
        // 先删除原有的关系信息
        // 前端没有传递setmealId,需要先查询出来
        Long id = setmealEntity.getId();

        QueryWrapper<SetmealDishEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id", id);
        setmealDishMapper.delete(queryWrapper);

        // 给setmealDish设置setmealId
        // 再插入新的关系信息
        for (SetmealDishEntity setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
            setmealDishMapper.insert(setmealDish);
        }

    }

    @Transactional
    @Override
    public void delete(String ids) {
        String[] idArr = ids.split(",");
        List<String> idList = Arrays.asList(idArr);
        log.info("idList:{}", idList);

        for (String id : idList) {
            SetmealEntity entity = setmealMapper.selectById(id);
            Integer status = entity.getStatus();
            if (status == 1) {
                throw new DeletionNotAllowedException("套餐已上架,不能删除");
            }
            setmealMapper.deleteById(id);
            QueryWrapper<SetmealDishEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("setmeal_id", id);
            setmealDishMapper.delete(queryWrapper);
        }
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        SetmealEntity setmealEntity = new SetmealEntity();
        setmealEntity.setId(id);
        setmealEntity.setStatus(status);
        setmealMapper.updateById(setmealEntity);
    }

}
