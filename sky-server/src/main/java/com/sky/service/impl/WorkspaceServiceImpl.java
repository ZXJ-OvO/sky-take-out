package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.sky.entity.OrdersEntity;
import com.sky.entity.UserEntity;
import com.sky.service.WorkspaceService;
import com.sky.utils.BigDecimalUtil;
import com.sky.vo.BusinessDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    /**
     * 获取当日工作台数据
     *
     * @return BusinessDataVO
     */
    @Override
    public BusinessDataVO getBusinessData() {
        // 新增用户数
        int newUsers = new LambdaQueryChainWrapper<>(UserEntity.class)
                .select(UserEntity::getOpenid)
                .between(UserEntity::getCreateTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now())
                .count()
                .intValue();

        // 有效订单数
        int validOrderCount = new LambdaQueryChainWrapper<>(OrdersEntity.class)
                .select(OrdersEntity::getId)
                .between(OrdersEntity::getOrderTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now())
                .eq(OrdersEntity::getStatus, OrdersEntity.COMPLETED)
                .count()
                .intValue();

        // 营业额
        double turnover = new LambdaQueryChainWrapper<>(OrdersEntity.class)
                .select(OrdersEntity::getAmount)
                .between(OrdersEntity::getOrderTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now())
                .eq(OrdersEntity::getStatus, OrdersEntity.COMPLETED)
                .list()
                .stream()
                .mapToDouble(ordersEntity -> ordersEntity.getAmount().doubleValue())
                .sum();

        double orderCompletionRate = 0;
        double unitPrice = 0;
        if (validOrderCount != 0) {
            // 平均客单价
            unitPrice = BigDecimalUtil.divide(turnover, validOrderCount, 2);
            // 订单完成率
            orderCompletionRate = BigDecimalUtil.divide(validOrderCount,
                    new LambdaQueryChainWrapper<>(OrdersEntity.class)
                            .select(OrdersEntity::getId)
                            .between(OrdersEntity::getOrderTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now())
                            .count());
        }

        // 返回VO
        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }
}
