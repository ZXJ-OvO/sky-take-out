package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.sky.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zxj
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
