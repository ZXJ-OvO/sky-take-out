package com.sky.controller.admin;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zxj
 */
@RestController
@RequestMapping("/admin/employee")
@Api(tags = "员工相关接口")
@Slf4j
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param employeeLoginDTO 员工信息DTO
     * @param httpServletRequest httpServletRequest
     * @return 员工信息VO
     */
    @ApiOperation(value = "员工登录", notes = "员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO, HttpServletRequest httpServletRequest) {
        EmployeeLoginVO employeeLoginVO = employeeService.login(employeeLoginDTO, httpServletRequest);
        return Result.success(employeeLoginVO);
    }

    /**
     * 员工登出
     *
     * @return null
     */
    @ApiOperation(value = "员工登出", notes = "员工登出")
    @PostMapping("/logout")
    public Result<String> login() {
        return Result.success();
    }
}
