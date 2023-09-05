package com.sky.service;


import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.EmployeeEntity;
import com.sky.result.PageBean;
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
    /**
     * 新增员工
     *
     * @param employeeLoginDTO 员工信息DTO
     */
    void insert(EmployeeDTO employeeLoginDTO);
    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO 员工分页查询DTO
     * @return 员工分页数据
     */
    PageBean pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
    /**
     * 根据id查询员工信息
     *
     * @param id 员工id
     * @return 员工信息
     */
    EmployeeEntity selectById(Long id);
    /**
     * 更新员工信息
     *
     * @param employeeDTO 员工信息DTO
     */
    void update(EmployeeDTO employeeDTO);
    /**
     * 更新员工状态
     *
     * @param status 状态
     * @param id     员工id
     */
    void updateStatus(Integer status, Long id);
    /**
     * 更新员工密码
     *
     * @param passwordEditDTO 密码修改DTO
     */
    void updatePassword(PasswordEditDTO passwordEditDTO);
}
