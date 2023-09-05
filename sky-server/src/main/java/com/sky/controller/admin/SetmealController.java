package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

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

    @PostMapping
    public Result<String> insert(@RequestBody SetmealDTO setmealDTO) {
        setmealService.insert(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> selectById(@PathVariable @NotNull(message = "套餐id不能为null") Long id) {
        SetmealVO setmealVO = setmealService.selectById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @DeleteMapping()
    public Result<String> delete(@RequestParam @NotNull(message = "套餐id不能为null") String ids) {
        setmealService.delete(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable @NotNull(message = "状态值不能为null") Integer status, @RequestParam @NotNull(message = "套餐id不能为null") Long id) {
        setmealService.updateStatus(status, id);
        return Result.success();
    }

}
