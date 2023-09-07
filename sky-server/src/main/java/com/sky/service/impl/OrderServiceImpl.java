package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBookEntity;
import com.sky.entity.OrderDetailEntity;
import com.sky.entity.OrdersEntity;
import com.sky.entity.ShoppingCartEntity;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.baomidou.mybatisplus.extension.toolkit.Db.saveBatch;

/**
 * 订单
 * @author zxj
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDetailMapper orderDetailMapper;
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private AddressBookMapper addressBookMapper;


    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        // 排除收获地址为空的情况
        AddressBookEntity addressBookEntity = addressBookMapper.selectById(ordersSubmitDTO.getAddressBookId());
        if (addressBookEntity == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 排除购物车为空的情况
        List<ShoppingCartEntity> shoppingCartEntities = new LambdaQueryChainWrapper<>(ShoppingCartEntity.class)
                .eq(ShoppingCartEntity::getUserId, BaseContext.getCurrentId()).list();
        if (shoppingCartEntities == null || shoppingCartEntities.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 排除超出配送范围的情况

        // 构造订单数据
        OrdersEntity ordersEntity = new OrdersEntity();
        BeanUtils.copyProperties(ordersSubmitDTO, ordersEntity);
        ordersEntity.setUserId(BaseContext.getCurrentId());
        ordersEntity.setPhone(addressBookEntity.getPhone());
        ordersEntity.setAddress(addressBookEntity.getDetail());
        ordersEntity.setConsignee(addressBookEntity.getConsignee());
        ordersEntity.setNumber(String.valueOf(System.currentTimeMillis()));
        ordersEntity.setStatus(OrdersEntity.PENDING_PAYMENT); // 订单状态：待付款
        ordersEntity.setPayStatus(OrdersEntity.UN_PAID); // 支付状态：未支付

        // 向订单表插入1条数据
        orderMapper.insert(ordersEntity);

        //订单明细数据
        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        for (ShoppingCartEntity cart : shoppingCartEntities) {
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(ordersEntity.getId());
            orderDetailList.add(orderDetail);
        }

        // 向明细表插入n条数据
        saveBatch(orderDetailList);

        //清理购物车中的数据
        shoppingCartMapper.delete(new LambdaQueryChainWrapper<>(ShoppingCartEntity.class)
                .eq(ShoppingCartEntity::getUserId, BaseContext.getCurrentId()).getWrapper());

        //封装返回结果
        return OrderSubmitVO.builder()
                .id(ordersEntity.getId())
                .orderNumber(ordersEntity.getNumber())
                .orderAmount(ordersEntity.getAmount())
                .build();
    }
}
