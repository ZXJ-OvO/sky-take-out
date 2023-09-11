package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderOverViewVO;

public interface WorkspaceService {

    BusinessDataVO getBusinessData();

    OrderOverViewVO getOverviewOrders();

}
