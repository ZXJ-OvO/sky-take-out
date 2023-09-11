package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrdersEntity;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author zxj
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrdersEntity> {
    /**
     * 根据订单号和用户id查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    OrdersEntity getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(OrdersEntity orders);

    OrderStatisticsVO statistic(Integer[] statusArray);

    List<GoodsSalesDTO> selectTopDish(LocalDateTime begin, LocalDateTime end);
}
