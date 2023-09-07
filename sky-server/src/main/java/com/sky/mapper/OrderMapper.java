package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.OrdersEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author zxj
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrdersEntity> {

}
