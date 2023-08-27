package com.sky.controller.admin;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
     *
     * @param employeeDTO 员工信息DTO
     * @return 员工信息VO
     */
    @PostMapping("/login")
    public Result login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        EmployeeLoginVO employeeLoginVO = employeeService.login(employeeLoginDTO);
        return Result.success(employeeLoginVO);
    }

}
