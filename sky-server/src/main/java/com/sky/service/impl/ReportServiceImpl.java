package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.TurnoverStatisticDTO;
import com.sky.entity.OrdersEntity;
import com.sky.entity.UserEntity;
import com.sky.exception.InvalidFieldException;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private WorkspaceService workspaceService;

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

    /**
     * 查询销量排名top10
     *
     * @param turnoverStatisticDTO 查询条件：开始日期、结束日期
     * @return 销量排名top10
     */
    @Override
    public SalesTop10ReportVO getTop10(TurnoverStatisticDTO turnoverStatisticDTO) {

        List<GoodsSalesDTO> list = orderMapper.selectTopDish(turnoverStatisticDTO.getBegin().atStartOfDay(),
                turnoverStatisticDTO.getEnd().atStartOfDay());

        List<String> nameList = list.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = list.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }

    /**
     * 订单统计
     *
     * @param turnoverStatisticDTO 查询条件：开始日期、结束日期
     * @return 订单统计结果
     */
    @Override
    public OrderReportVO getOrderStatistics(TurnoverStatisticDTO turnoverStatisticDTO) {

        LocalDate begin = turnoverStatisticDTO.getBegin();
        LocalDate end = turnoverStatisticDTO.getEnd();

        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> validOrderList = new ArrayList<>();
        List<Integer> totalOrderList = new ArrayList<>();

        while (!begin.isAfter(end)) {
            LocalDateTime startTime = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(begin, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("status", OrdersEntity.COMPLETED);
            map.put("begin", startTime);
            map.put("end", endTime);
            Integer validOrderCount = orderMapper.countByMap(map);
            map.put("status", null);
            Integer totalOrderCount = orderMapper.countByMap(map);

            dateList.add(begin);
            validOrderList.add(validOrderCount);
            totalOrderList.add(totalOrderCount);

            begin = begin.plusDays(1);
        }

        int validOrderCount = validOrderList.stream().mapToInt(i -> i).sum();
        int totalOrderCount = totalOrderList.stream().mapToInt(i -> i).sum();

        double orderCompletionRate = 0D;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount * 1.0 / totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .validOrderCountList(StringUtils.join(validOrderList, ","))
                .orderCountList(StringUtils.join(totalOrderList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 输出报表 ：HttpServletResponse
     */
    @Override
    @SneakyThrows
    public void getExport(HttpServletResponse httpServletResponse) {
        // 获取开始日期和结束日期
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);

        //查询概览运营数据，提供给Excel模板文件
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        //基于提供好的模板文件创建一个新的Excel表格对象
        assert inputStream != null;
        XSSFWorkbook excel = new XSSFWorkbook(inputStream);

        //获得Excel文件中的一个Sheet页
        XSSFSheet sheet = excel.getSheet("Sheet1");

        sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);

        //获得第4行
        XSSFRow row = sheet.getRow(3);

        //获取单元格
        row.getCell(2).setCellValue(businessData.getTurnover());
        row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
        row.getCell(6).setCellValue(businessData.getNewUsers());

        //获得第5行
        row = sheet.getRow(4);
        row.getCell(2).setCellValue(businessData.getValidOrderCount());
        row.getCell(4).setCellValue(businessData.getUnitPrice());

        for (int i = 0; i < 30; i++) {
            LocalDate date = begin.plusDays(i);
            //准备明细数据
            businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
            row = sheet.getRow(7 + i);
            row.getCell(1).setCellValue(date.toString());
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(3).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(5).setCellValue(businessData.getUnitPrice());
            row.getCell(6).setCellValue(businessData.getNewUsers());
        }

        //通过输出流将文件下载到客户端浏览器中
        ServletOutputStream out = httpServletResponse.getOutputStream();
        excel.write(out);

        //关闭资源
        out.close();
        excel.close();
    }
}
