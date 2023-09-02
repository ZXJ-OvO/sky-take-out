package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.github.yulichang.base.MPJBaseMapper;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishEntity;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zxj
 */
@Mapper
public interface DishMapper extends BaseMapper<DishEntity> {
    Page<DishVO> pageQuery(DishPageQueryDTO dto);
}
