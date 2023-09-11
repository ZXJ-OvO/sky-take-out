package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.dto.TurnoverStatisticDTO;
import com.sky.entity.OrdersEntity;
import com.sky.entity.UserEntity;
import com.sky.exception.InvalidFieldException;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 获取当天的营业额
     *
     * @param date 日期
     * @return 营业额
     */
    private Double getTurnoverForDate(LocalDate date) {
        QueryWrapper<OrdersEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(amount) as amount");
        queryWrapper.eq("status", OrdersEntity.COMPLETED);
        queryWrapper.between("order_time", date.atStartOfDay(), date.atStartOfDay().plusDays(1));

        OrdersEntity ordersEntity = orderMapper.selectOne(queryWrapper);

        return (ordersEntity != null && ordersEntity.getAmount() != null)
                ? ordersEntity.getAmount().doubleValue()
                : 0.0;
    }

    /**
     * 营业额统计
     *
     * @param turnoverStatisticDTO 查询条件：开始日期、结束日期
     * @return 营业额统计结果
     */
    @Override
    public TurnoverReportVO getTurnoverReport(TurnoverStatisticDTO turnoverStatisticDTO) {
        // 获取查询日期的下限
        LocalDate begin = turnoverStatisticDTO.getBegin();
        // 创建数据库查询数据的日期列表
        List<LocalDate> dateList = new ArrayList<>();
        // 创建数据库查询数据的营业额列表
        List<Double> turnoverList = new ArrayList<>();

        // 使用isAfter()取反而不是使用isBefore()，是因为isBefore()不包含边界值
        // 取出begin日期到end日期之间的所有日期
        while (!begin.isAfter(turnoverStatisticDTO.getEnd())) {
            // 把当前的天数添加到日期列表中
            dateList.add(begin);
            // 获得当前日期对应的营业额
            Double turnover = getTurnoverForDate(begin);
            // 把当前的营业额添加到营业额列表中
            turnoverList.add(turnover);
            // 把日期加1天，进入循环之中继续获取下一天的日期和营业额
            begin = begin.plusDays(1);
        }

        if (dateList.isEmpty()) {
            throw new InvalidFieldException("指定日期范围内没有数据");
        }

        // StringUtils.join()方法可以把列表中的元素用指定的分隔符连接起来，由commons-lang3提供
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }


    /**
     * 获取用户统计数据
     *
     * @param date    日期
     * @param isTotal 是否是求总数
     * @return 用户统计数据
     */
    private Integer getUserReportData(LocalDate date, Boolean isTotal) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("openid");
        if (isTotal) {
            queryWrapper.lt("create_time", date.atStartOfDay().plusDays(1));
        } else {
            queryWrapper.between("create_time", date.atStartOfDay(), date.atStartOfDay().plusDays(1));
        }
        return userMapper.selectCount(queryWrapper).intValue();
    }


    /**
     * 用户统计
     * @param turnoverStatisticDTO 查询条件：开始日期、结束日期
     * @return 用户统计结果
     */
    @Override
    public UserReportVO getUserStatistics(TurnoverStatisticDTO turnoverStatisticDTO) {
        LocalDate begin = turnoverStatisticDTO.getBegin();
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        while (!begin.isAfter(turnoverStatisticDTO.getEnd())) {
            Integer newUser = getUserReportData(begin, false);
            Integer totalUser = getUserReportData(begin, true);

            dateList.add(begin);
            newUserList.add(newUser);
            totalUserList.add(totalUser);

            begin = begin.plusDays(1);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }


}
