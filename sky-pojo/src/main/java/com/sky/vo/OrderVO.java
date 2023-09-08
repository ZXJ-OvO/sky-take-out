package com.sky.vo;

import com.sky.entity.OrderDetailEntity;
import com.sky.entity.OrdersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends OrdersEntity implements Serializable {
    /**
     * OrderVO 继承至 OrdersEntity
     * OrderVO extends OrdersEntity
     */
    private String orderDishes;   //订单菜品信息
    private List<OrderDetailEntity> orderDetailList;    //订单详情
}
