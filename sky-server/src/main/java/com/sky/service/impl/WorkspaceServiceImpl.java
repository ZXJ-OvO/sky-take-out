package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.sky.entity.DishEntity;
import com.sky.entity.OrdersEntity;
import com.sky.entity.SetmealEntity;
import com.sky.entity.UserEntity;
import com.sky.service.WorkspaceService;
import com.sky.utils.BigDecimalUtil;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
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

    /**
     * 查询订单管理数据
     *
     * @return BusinessDataVO
     */
    @Override
    public OrderOverViewVO getOverviewOrders() {

        return OrderOverViewVO.builder()
                .allOrders(new LambdaQueryChainWrapper<>(OrdersEntity.class)
                        .select(OrdersEntity::getId)
                        .between(OrdersEntity::getOrderTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now())
                        .count()
                        .intValue())
                .cancelledOrders(new LambdaQueryChainWrapper<>(OrdersEntity.class)
                        .select(OrdersEntity::getId)
                        .between(OrdersEntity::getOrderTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now())
                        .eq(OrdersEntity::getStatus, OrdersEntity.CANCELLED)
                        .count()
                        .intValue())
                .completedOrders(new LambdaQueryChainWrapper<>(OrdersEntity.class)
                        .select(OrdersEntity::getId)
                        .between(OrdersEntity::getOrderTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now())
                        .eq(OrdersEntity::getStatus, OrdersEntity.COMPLETED)
                        .count()
                        .intValue())
                .deliveredOrders(new LambdaQueryChainWrapper<>(OrdersEntity.class)
                        .select(OrdersEntity::getId)
                        .between(OrdersEntity::getOrderTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now())
                        .eq(OrdersEntity::getStatus, OrdersEntity.CONFIRMED)
                        .count()
                        .intValue())
                .waitingOrders(new LambdaQueryChainWrapper<>(OrdersEntity.class)
                        .select(OrdersEntity::getId)
                        .between(OrdersEntity::getOrderTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now())
                        .eq(OrdersEntity::getStatus, OrdersEntity.TO_BE_CONFIRMED)
                        .count()
                        .intValue())
                .build();
    }

    /**
     * 查询菜品管理数据
     *
     * @return DishOverViewVO
     */
    @Override
    public DishOverViewVO getOverviewDishes() {

        return DishOverViewVO.builder()
                .sold(new LambdaQueryChainWrapper<>(DishEntity.class)
                        .select(DishEntity::getId)
                        .eq(DishEntity::getStatus, 1)
                        .count()
                        .intValue())
                .discontinued(new LambdaQueryChainWrapper<>(DishEntity.class)
                        .select(DishEntity::getId)
                        .eq(DishEntity::getStatus, 0)
                        .count()
                        .intValue())
                .build();
    }

    /**
     * 查询套餐管理数据
     *
     * @return SetmealOverViewVO
     */
    @Override
    public SetmealOverViewVO getOverviewSetMeals() {
        return SetmealOverViewVO.builder()
                .sold(new LambdaQueryChainWrapper<>(SetmealEntity.class)
                        .select(SetmealEntity::getId)
                        .eq(SetmealEntity::getStatus, 1)
                        .count()
                        .intValue())
                .discontinued(new LambdaQueryChainWrapper<>(SetmealEntity.class)
                        .select(SetmealEntity::getId)
                        .eq(SetmealEntity::getStatus, 0)
                        .count()
                        .intValue())
                .build();
    }
}
