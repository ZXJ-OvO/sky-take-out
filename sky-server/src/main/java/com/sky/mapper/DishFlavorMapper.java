package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.DishFlavorEntity;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zxj
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavorEntity> {

    List<DishFlavorEntity> getByIdWithFlavor(Long id);

    List<DishFlavorEntity> getByDishId(Long dishId);
}
