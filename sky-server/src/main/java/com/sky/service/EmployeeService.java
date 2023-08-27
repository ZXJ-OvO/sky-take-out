package com.sky.service;


import com.sky.dto.EmployeeLoginDTO;
import com.sky.vo.EmployeeLoginVO;

/**
 * @author zxj
 */
public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeDTO 员工信息DTO
     * @return 员工信息VO
     */
    EmployeeLoginVO login(EmployeeLoginDTO employeeDTO);
}
