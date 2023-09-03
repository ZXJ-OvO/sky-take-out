package com.sky.controller.admin;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    @GetMapping("/page")
    public Result<PageBean> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageBean pageBean = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageBean);
    }
}
