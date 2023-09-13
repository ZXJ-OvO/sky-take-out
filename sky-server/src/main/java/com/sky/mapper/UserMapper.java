package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author zxj
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    /**
     * 根据id查询用户
     *
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    UserEntity getById(Long userId);

    Integer countByMap(Map map);
}
