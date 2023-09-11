package com.sky.service;

import com.sky.dto.TurnoverStatisticDTO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

public interface ReportService {

    TurnoverReportVO getTurnoverReport(TurnoverStatisticDTO turnoverStatisticDTO);

    UserReportVO getUserStatistics(TurnoverStatisticDTO turnoverStatisticDTO);
}
