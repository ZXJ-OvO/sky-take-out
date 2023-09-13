package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishEntity;
import com.sky.entity.DishFlavorEntity;
import com.sky.entity.SetmealDishEntity;
import com.sky.entity.SetmealEntity;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageBean;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author zxj
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Resource
    private DishMapper dishMapper;

    @Resource
    private DishFlavorMapper dishFlavorMapper;

    @Resource
    private SetmealDishMapper setmealDishMapper;

    @Resource
    private SetmealMapper setmealMapper;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 清理缓存数据
     *
     * @param pattern
     */
    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * 菜品状态
     *
     * @param status 0停售 1起售
     * @param id     菜品id
     */
    @Override
    @Transactional
    public void updateStatus(Integer status, Long id) {
        UpdateWrapper<DishEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("status", status);
        dishMapper.update(null, updateWrapper);

        // 同步修改套餐关联菜品状态
        List<Long> setmealIds = setmealDishMapper.selectSetmealIds(id);

        if (!setmealIds.isEmpty()) {
            for (Long setmealId : setmealIds) {
                SetmealEntity setmealEntity = new SetmealEntity();
                setmealEntity.setId(setmealId);
                setmealEntity.setStatus(status);
                setmealMapper.updateById(setmealEntity);
            }
        }

        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");

    }

    /**
     * 分页查询
     *
     * @param dishPageQueryDTO 分页查询条件
     * @return 分页结果
     */
    @Override
    public PageBean pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //1. 开启分页
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //2. 调用mapper分页条件查询
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        log.info("分页查询结果:{}", page.getResult());

        return PageBean.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 根据id查询菜品
     *
     * @param id 菜品id
     * @return 菜品信息
     */
    @Override
    public DishVO selectById(Long id) {
        DishEntity dishEntity = dishMapper.selectById(id);

        List<DishFlavorEntity> dishFlavorEntities = dishFlavorMapper.getByDishId(id);

        DishVO dishVO = new DishVO();

        BeanUtils.copyProperties(dishEntity, dishVO);

        dishVO.setFlavors(dishFlavorEntities);

        return dishVO;
    }

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品信息
     */
    @Override
    @Transactional
    public void insertWithFlavor(DishDTO dishDTO) {
        DishEntity dishEntity = new DishEntity();
        BeanUtils.copyProperties(dishDTO, dishEntity);

        dishEntity.setStatus(StatusConstant.DISABLE);
        dishMapper.insert(dishEntity);

        List<DishFlavorEntity> flavors = dishDTO.getFlavors();

        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishEntity.getId());
                dishFlavorMapper.insert(dishFlavor);
            });
        }

        //清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);

    }

    /**
     * 批量删除菜品
     *
     * @param ids 菜品id数组
     */
    @Override
    public void batchDeleteByIds(String[] ids) {
        //1. 判断是否有菜品正在售卖 select * from dish where id in (ids) and status=1
        List<DishEntity> dishes = dishMapper.selectBatchIds(Arrays.asList(ids));

        if (dishes.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_NOT_EXIST);
        }

        List<String> idList = Arrays.asList(ids);

        //2. 判断是否有套餐关联菜品
        for (String id : ids) {
            QueryWrapper<SetmealDishEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dish_id", id);
            List<SetmealDishEntity> setmealDishEntities = setmealDishMapper.selectList(queryWrapper);
            if (!setmealDishEntities.isEmpty()) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }

        //3. 删除菜品，删除口味
        dishMapper.deleteBatchIds(idList);
        dishFlavorMapper.deleteBatchIds(idList);

    }

    /**
     * 修改菜品
     *
     * @param dishDTO 菜品信息
     */
    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        DishEntity dish = new DishEntity();
        BeanUtils.copyProperties(dishDTO, dish);

        //1. 更新菜品主体数据
        dishMapper.updateById(dish);
        //2. 删除原来口味
        Long id = dishDTO.getId();

        QueryWrapper<DishFlavorEntity> flavourWrapper = new QueryWrapper<>();
        flavourWrapper.eq("dish_id", id);
        List<DishFlavorEntity> flavorEntities = dishFlavorMapper.selectList(flavourWrapper);
        if (!flavorEntities.isEmpty()) {
            for (DishFlavorEntity flavorEntity : flavorEntities) {
                dishFlavorMapper.deleteById(flavorEntity);
            }
        }

        //3. 添加新口味
        List<DishFlavorEntity> flavors = dishDTO.getFlavors();

        flavors.forEach(dishFlavor -> {
            dishFlavor.setDishId(id);

        });
        dishFlavorMapper.batchInsert(flavors);

        // 需要修改套餐关联菜品的数据
        QueryWrapper<SetmealDishEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id", id);
        List<SetmealDishEntity> list = setmealDishMapper.selectList(queryWrapper);
        list.forEach(setmealDishEntity -> {
            String name = dish.getName();
            BigDecimal price = dish.getPrice();
            setmealDishEntity.setName(name);
            setmealDishEntity.setPrice(price);
            setmealDishMapper.updateById(setmealDishEntity);
        });

        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
    }

    /**
     * （管理端）根据菜品分类id查询菜品
     *
     * @param categoryId 菜品分类id
     * @return 根据分类id查出来的分类下的所有菜品/套餐数据
     */
    @Override
    public List<DishEntity> listByCategoryId(Long categoryId) {
        return new LambdaQueryChainWrapper<>(dishMapper)
                .eq(DishEntity::getCategoryId, categoryId)
                .list();
    }

    /**
     * （用户端）根据菜品分类id查询菜品
     *
     * @param categoryId 菜品/套餐分类id
     * @return 根据分类id查出来的分类下的所有菜品/套餐数据
     */
    @Override
    @Cacheable(cacheNames = "setmeal:list", key = "#categoryId", unless = "#result.size() <= 0")
    public List<DishVO> selectByCategoryId(Long categoryId) {

        // 查数据库
        LambdaQueryWrapper<DishEntity> lambdaQueryWrapper = new LambdaQueryWrapper<DishEntity>()
                .eq(DishEntity::getCategoryId, categoryId)
                .eq(DishEntity::getStatus, 1);
        List<DishEntity> entities = dishMapper.selectList(lambdaQueryWrapper);

        if (entities == null || entities.isEmpty()) {
            return null;
        }

        List<DishVO> dishVos = new ArrayList<>();
        for (DishEntity entity : entities) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(entity, dishVO);
            dishVos.add(dishVO);
        }
        for (DishVO dishVo : dishVos) {
            Long id = dishVo.getId();
            LambdaQueryWrapper<DishFlavorEntity> wrapper = new LambdaQueryWrapper<DishFlavorEntity>().eq(DishFlavorEntity::getDishId, id);
            List<DishFlavorEntity> flavorEntities = dishFlavorMapper.selectList(wrapper);
            dishVo.setFlavors(flavorEntities);
        }

        return dishVos;
    }
}
