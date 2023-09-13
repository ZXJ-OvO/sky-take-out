package com.sky.task;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.sky.constant.MessageConstant;
import com.sky.entity.OrdersEntity;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/09/09 15:43
 */
@Component
@Slf4j
public class OrderTask {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     * cron表达式需要根据业务实际需求调整，控制最佳粒度
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeoutOrder() {
        List<OrdersEntity> ordersEntityList = new LambdaQueryChainWrapper<>(OrdersEntity.class)
                .eq(OrdersEntity::getStatus, OrdersEntity.PENDING_PAYMENT)
                .lt(OrdersEntity::getOrderTime, LocalDateTime.now().minusMinutes(15))
                .list();

        if (ordersEntityList != null && !ordersEntityList.isEmpty()) {
            for (OrdersEntity ordersEntity : ordersEntityList) {
                ordersEntity.setStatus(OrdersEntity.CANCELLED);
                ordersEntity.setCancelReason(MessageConstant.TIME_OUT_PAYMENT_RANGE);
                ordersEntity.setCancelTime(LocalDateTime.now());
                orderMapper.updateById(ordersEntity);
                log.info("处理了一笔超时未支付的订单，订单号为：{}", ordersEntity.getNumber());
            }
        }
    }


    /**
     * 处理始终处于派送中状态的订单
     * 每天凌晨一点执行
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void processAlwaysDeliveryOrder() {
        List<OrdersEntity> ordersEntityList = new LambdaQueryChainWrapper<>(OrdersEntity.class)
                .eq(OrdersEntity::getStatus, OrdersEntity.DELIVERY_IN_PROGRESS)
                .lt(OrdersEntity::getOrderTime, LocalDateTime.now().minusHours(1))
                .list();

        if (ordersEntityList != null && !ordersEntityList.isEmpty()) {
            for (OrdersEntity ordersEntity : ordersEntityList) {
                ordersEntity.setStatus(OrdersEntity.COMPLETED);
                orderMapper.updateById(ordersEntity);
                log.info("处理了一笔未及时更新配送状态的订单，订单号为：{}", ordersEntity.getNumber());
            }
        }
    }

}
