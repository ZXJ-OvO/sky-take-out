package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.EmployeeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zxj
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<EmployeeEntity> {
}
