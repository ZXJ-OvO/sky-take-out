package com.sky.service;

import com.sky.dto.TurnoverStatisticDTO;
import com.sky.vo.TurnoverReportVO;

public interface ReportService {

    TurnoverReportVO getTurnoverReport(TurnoverStatisticDTO turnoverStatisticDTO);
}
