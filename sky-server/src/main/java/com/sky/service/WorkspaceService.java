package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    BusinessDataVO getBusinessData();

    OrderOverViewVO getOverviewOrders();

    DishOverViewVO getOverviewDishes();

    SetmealOverViewVO getOverviewSetMeals();

    BusinessDataVO getBusinessData(LocalDateTime of, LocalDateTime of1);
}
