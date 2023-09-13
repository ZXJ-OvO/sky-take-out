package com.sky.service;

import com.sky.dto.TurnoverStatisticDTO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {

    TurnoverReportVO getTurnoverReport(TurnoverStatisticDTO turnoverStatisticDTO);

    UserReportVO getUserStatistics(TurnoverStatisticDTO turnoverStatisticDTO);

    SalesTop10ReportVO getTop10(TurnoverStatisticDTO turnoverStatisticDTO);

    OrderReportVO getOrderStatistics(TurnoverStatisticDTO turnoverStatisticDTO);

    void getExport(HttpServletResponse httpServletResponse);

}
