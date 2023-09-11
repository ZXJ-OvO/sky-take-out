package com.sky.controller.admin;

import com.sky.dto.TurnoverStatisticDTO;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "统计分析相关接口")
public class ReportController {

    @Resource
    private ReportService reportService;

    /**
     * 营业额统计
     *
     * @param turnoverStatisticDTO 查询条件：开始日期、结束日期
     * @return 营业额统计结果
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistic(TurnoverStatisticDTO turnoverStatisticDTO) {
        TurnoverReportVO turnoverReportVO = reportService.getTurnoverReport(turnoverStatisticDTO);
        return Result.success(turnoverReportVO);
    }

    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(TurnoverStatisticDTO turnoverStatisticDTO) {
        UserReportVO userReportVO = reportService.getUserStatistics(turnoverStatisticDTO);
        return Result.success(userReportVO);
    }

    @GetMapping("/top10")
    @ApiOperation("查询销量排名top10")
    public Result<SalesTop10ReportVO> top10(TurnoverStatisticDTO turnoverStatisticDTO) {
        SalesTop10ReportVO salesTop10ReportVO = reportService.getTop10(turnoverStatisticDTO);
        return Result.success(salesTop10ReportVO);
    }

}
