package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageBean;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * @author zxj
 */
public interface OrderService {

    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    PageBean pageQueryHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO queryOrderDetail(Long id);

    void onceAgainThisOrder(Long id);

    void cancelOrderById(Long id);

    void urgedOrderById(Long id);

    PageBean adminConditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statisticsEachItemNumber();

    OrderVO getDetailsById(Long id);

}
