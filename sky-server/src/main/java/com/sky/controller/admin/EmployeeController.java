package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 员工信息DTO
     * @return 员工信息VO
     */
    @ApiOperation(value = "新增员工", notes = "新增员工")
    @PostMapping
    public Result<String> insert(@RequestBody @Validated EmployeeDTO employeeDTO) {
        employeeService.insert(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO 员工信息DTO
     * @return pageBean 员工信息分页对象
     */
    @ApiOperation(value = "员工分页查询", notes = "员工分页查询")
    @GetMapping("/page")
    public Result<PageBean> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageBean pageBean = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageBean);
    }
}
