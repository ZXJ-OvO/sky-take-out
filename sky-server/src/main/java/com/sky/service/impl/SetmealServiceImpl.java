package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.support.result.WordResultHandlers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.CategoryEntity;
import com.sky.entity.DishEntity;
import com.sky.entity.SetmealDishEntity;
import com.sky.entity.SetmealEntity;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.UpdateSetmealStatusException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageBean;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
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

    @Resource
    private DishMapper dishMapper;

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
        // 根据id查询套餐
        SetmealEntity entity = setmealMapper.selectById(id);
        // 查到的主体数据交给vo，查到的数据没有套餐菜品的List，没有分类名称
        SetmealVO vo = new SetmealVO();
        BeanUtils.copyProperties(entity, vo);

        // 根据categoryId查询分类名称
        Long categoryId = entity.getCategoryId();
        CategoryEntity categoryEntity = categoryMapper.selectById(categoryId);
        String name = categoryEntity.getName();
        vo.setCategoryName(name);

        // 查询套餐菜品关系信息
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

        if (status == 1) {
            // 起售需要判断套餐中是否存在菜品是禁售的状态（状态值为0）
            QueryWrapper<SetmealDishEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("setmeal_id", id);
            List<SetmealDishEntity> setmealDishEntities = setmealDishMapper.selectList(queryWrapper);
            for (SetmealDishEntity setmealDishEntity : setmealDishEntities) {
                Long dishId = setmealDishEntity.getDishId();
                DishEntity dishEntity = dishMapper.selectById(dishId);
                Integer dishStatus = dishEntity.getStatus();
                if (dishStatus == 0) {
                    throw new UpdateSetmealStatusException("套餐中存在禁售的菜品,不能上架");
                }
            }
        }

        SetmealEntity setmealEntity = new SetmealEntity();
        setmealEntity.setId(id);
        setmealEntity.setStatus(status);
        setmealMapper.updateById(setmealEntity);
    }

    @Override
    public List<SetmealEntity> selectByCategoryId(Long categoryId) {

        LambdaQueryWrapper<SetmealEntity> lambdaQueryWrapper = new LambdaQueryWrapper<SetmealEntity>()
                .eq(SetmealEntity::getCategoryId, categoryId)
                .eq(SetmealEntity::getStatus, 1);
        List<SetmealEntity> entities = setmealMapper.selectList(lambdaQueryWrapper);
        return entities;
    }

    @Override
    public List<DishItemVO> selectSetmealDishes(Long id) {
        List<DishItemVO> dishItemVos = setmealDishMapper.selectSetmealIncludeDishes(id);
        return dishItemVos;
    }

}
