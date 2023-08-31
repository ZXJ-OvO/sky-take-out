package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.EmployeeEntity;
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
import javax.validation.constraints.NotNull;

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
    /**
     * 根据id查询员工
     *
     * @param id 员工id
     * @return 员工信息
     */
    @ApiOperation(value = "根据id查询员工", notes = "根据id查询员工")
    @GetMapping("/{id}")
    public Result<EmployeeEntity> selectById(@PathVariable Long id) {
        EmployeeEntity employee = employeeService.selectById(id);
        return Result.success(employee);
    }
    /**
     * 更新员工
     *
     * @param employeeDTO 员工信息DTO
     * @return null
     */
    @ApiOperation(value = "更新员工", notes = "编辑员工信息")
    @PutMapping
    public Result<String> update(@RequestBody @Validated EmployeeDTO employeeDTO) {
        employeeService.update(employeeDTO);
        return Result.success();
    }
    /**
     * 账号状态
     *
     * @param status 0：禁用 1：启用
     * @param id     员工id
     * @return null
     */
    @ApiOperation(value = "账号状态", notes = "员工账号禁用与启用操作")
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable @NotNull(message = "状态不能为空") Integer status, @RequestParam @NotNull(message = "id不能为空") Long id) {
        employeeService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 修改密码
     *
     * @param passwordEditDTO 密码修改DTO
     * @return null
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PutMapping("/editPassword")
    public Result<String> updatePassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        employeeService.updatePassword(passwordEditDTO);
        return Result.success();
    }
}
