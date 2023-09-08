package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageBean;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.SneakyThrows;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.extension.toolkit.Db.saveBatch;
import static com.sky.entity.OrdersEntity.*;

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
                .status(TO_BE_CONFIRMED)
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

        long count = records.size();  // FIXME: 2023/9/8 怀疑有问题
        return PageBean.builder().total(count).records(records).build();
    }

    @Override
    public OrderVO queryOrderDetail(Long id) {
        OrdersEntity orderEntity = new LambdaQueryChainWrapper<>(OrdersEntity.class)
                .eq(OrdersEntity::getId, id)
                .one();

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderEntity, orderVO);
        orderVO.setOrderDetailList(
                orderDetailMapper.selectList(
                        new LambdaQueryWrapper<OrderDetailEntity>()
                                .eq(OrderDetailEntity::getOrderId, orderEntity.getId()))
        );

        return orderVO;
    }

    @Override
    public void onceAgainThisOrder(Long id) {
        queryOrderDetail(id).getOrderDetailList().forEach(orderDetailEntity -> {
            // 再来一旦的时候，订单明细的每个商品的id都要置空，因为此时拿到了旧数据的id，再次插入会引发主键冲突
            orderDetailEntity.setId(null);
            ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
            BeanUtils.copyProperties(orderDetailEntity, shoppingCartEntity);
            shoppingCartEntity.setUserId(BaseContext.getCurrentId());
            shoppingCartMapper.insert(shoppingCartEntity);
        });
    }

    @SneakyThrows
    @Transactional
    @Override
    public void cancelOrderById(Long id) {
        //  1. 校验订单是否存在
        OrdersEntity ordersEntity = orderMapper.selectById(id);
        if (ordersEntity == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //  2. 校验订单状态：仅待付款和待接单状态的订单可以直接取消，其他状态的不能取消
        if (!OrdersEntity.PENDING_PAYMENT.equals(ordersEntity.getStatus())
                && !TO_BE_CONFIRMED.equals(ordersEntity.getStatus())) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //  3. 如果订单状态为待接单且支付状态为已支付的情况下，还需要进行退款操作，需要做如下操作：

        //  - 调用weChatUtil.refund方法退款
        // weChatPayUtil.refund(ordersEntity.getNumber(), "商户退款单号", ordersEntity.getAmount(), ordersEntity.getAmount());

        //  - 将支付状态改为退款
        ordersEntity.setPayStatus(OrdersEntity.REFUND);

        //  4. 最后取消订单，其实就是更新订单状态为已取消（可以将取消原因设置为：用户取消）
        ordersEntity.setStatus(OrdersEntity.CANCELLED);

        orderMapper.updateById(ordersEntity);
    }

    @Override
    public void urgedOrderById(Long id) {
        // TODO: 2023/9/8 催单
    }

    @Override
    public PageBean adminConditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        Integer status = ordersPageQueryDTO.getStatus();
        String phone = ordersPageQueryDTO.getPhone();
        String number = ordersPageQueryDTO.getNumber();
        LocalDateTime beginTime = ordersPageQueryDTO.getBeginTime();
        LocalDateTime endTime = ordersPageQueryDTO.getEndTime();
        // 通过对前台页面的测试发现，beginTime和endTime必须同时存在或者同时不被携带，因此做并且判断
        Page<OrdersEntity> page = new LambdaQueryChainWrapper<>(OrdersEntity.class)
                .eq(status != null, OrdersEntity::getStatus, status)
                .eq(phone != null, OrdersEntity::getPhone, phone)
                .eq(number != null, OrdersEntity::getNumber, number)
                .between(beginTime != null && endTime != null, OrdersEntity::getOrderTime, beginTime, endTime)
                .page(new Page<>(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize()));

        List<OrdersEntity> records = page.getRecords();
        long total = page.getTotal();

        List<OrderVO> orderVOs = new ArrayList<>();
        records.stream().forEach(orderEntity -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orderEntity, orderVO);

            List<OrderDetailEntity> orderDetailEntities = new LambdaQueryChainWrapper<>(OrderDetailEntity.class)
                    .eq(OrderDetailEntity::getOrderId, orderEntity.getId())
                    .list();

            List<String> names = orderDetailEntities.stream()
                    .map(OrderDetailEntity::getName)
                    .distinct()
                    .collect(Collectors.toList());

            String orderDishes = String.join(",", names);
            orderVO.setOrderDishes(orderDishes);
            orderVOs.add(orderVO);
        });
        return PageBean.builder().total(total).records(orderVOs).build();
    }

    @Override
    public OrderStatisticsVO statisticsEachItemNumber() {
        return orderMapper.statistic(new Integer[]{TO_BE_CONFIRMED, CONFIRMED, DELIVERY_IN_PROGRESS});
    }

    @Override
    public OrderVO getDetailsById(Long id) {
        return queryOrderDetail(id);
    }

    @Override
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        orderMapper.updateById(OrdersEntity.builder().id(ordersConfirmDTO.getId()).status(CONFIRMED).build());
    }

    @Transactional
    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        // 支付完成的订单才会被修改成待接单，拒单只发生在待接单里，因此需要拒单并退款
        // 退款  weChatPayUtil.refund()
        orderMapper.updateById(OrdersEntity.builder()
                .id(ordersRejectionDTO.getId())
                .status(CANCELLED)
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .payStatus(REFUND)
                .build());
    }

    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        // 查询支付状态，已支付的先完成退款
        OrdersEntity ordersEntity = orderMapper.selectById(ordersCancelDTO.getId());
        if (ordersEntity == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        if (Objects.equals(ordersEntity.getPayStatus(), PAID)) {
            // 退款  weChatPayUtil.refund()
            orderMapper.updateById(OrdersEntity.builder()
                    .id(ordersEntity.getId())
                    .cancelReason(ordersCancelDTO.getCancelReason())
                    .status(CANCELLED)
                    .payStatus(REFUND)
                    .build()
            );
        } else {
            // 没有付款的订单
            orderMapper.updateById(OrdersEntity.builder()
                    .id(ordersEntity.getId())
                    .cancelReason(ordersCancelDTO.getCancelReason())
                    .status(CANCELLED)
                    .payStatus(UN_PAID)
                    .build()
            );
        }

    }
}
