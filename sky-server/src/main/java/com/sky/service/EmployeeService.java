package com.sky.service;


import com.sky.dto.EmployeeLoginDTO;
import com.sky.vo.EmployeeLoginVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zxj
 */
public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeDTO        员工信息DTO
     * @param httpServletRequest httpServletRequest
     * @return 员工信息VO
     */
    EmployeeLoginVO login(EmployeeLoginDTO employeeDTO, HttpServletRequest httpServletRequest);
}
