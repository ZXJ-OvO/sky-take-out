package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageBean;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;


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

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        UserEntity user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && "ORDERPAID".equals(jsonObject.getString("code"))) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     */
    public void paySuccess(String outTradeNo) {
        // 根据订单号查询当前用户的订单
        OrdersEntity ordersDb = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        OrdersEntity orders = OrdersEntity.builder()
                .id(ordersDb.getId())
                .status(OrdersEntity.TO_BE_CONFIRMED)
                .payStatus(OrdersEntity.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }


    @Override
    public PageBean pageQueryHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {

        List<OrdersEntity> ordersEntityRecords = new LambdaQueryChainWrapper<>(OrdersEntity.class)
                .eq(OrdersEntity::getUserId, BaseContext.getCurrentId())
                .eq(ordersPageQueryDTO.getStatus() != null, OrdersEntity::getStatus, ordersPageQueryDTO.getStatus())
                .orderByDesc(OrdersEntity::getOrderTime)
                .page(new Page<>(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize()))
                .getRecords();

        List<OrderVO> records = new ArrayList<>();

        ordersEntityRecords.forEach(ordersEntity -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(ordersEntity, orderVO);
            orderVO.setOrderDetailList(
                    orderDetailMapper.selectList(
                            new LambdaQueryChainWrapper<>(OrderDetailEntity.class)
                                    .eq(OrderDetailEntity::getOrderId, ordersEntity.getId()).getWrapper()));
            records.add(orderVO);
        });

        long count = records.size();
        return PageBean.builder().total(count).records(records).build();
    }
}
