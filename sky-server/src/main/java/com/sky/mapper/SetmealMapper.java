package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealEntity;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author zxj
 */
@Mapper
public interface SetmealMapper extends BaseMapper<SetmealEntity> {

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
