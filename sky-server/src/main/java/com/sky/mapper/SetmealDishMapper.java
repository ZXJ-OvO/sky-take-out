package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.SetmealDishEntity;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDishEntity> {

    List<Long> selectSetmealIds(Long id);

    List<DishItemVO> selectSetmealIncludeDishes(Long id);
}
