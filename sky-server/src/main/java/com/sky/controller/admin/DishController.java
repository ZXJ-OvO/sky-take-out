package com.sky.controller.admin;

import com.sky.annotation.PreAuthorize;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishEntity;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author root
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Resource
    private DishService dishService;


    @ApiOperation(value = "菜品状态", notes = "菜品起售、停售")
    @PostMapping("/status/{status}")
    @PreAuthorize("admin:category:status")
    public Result<String> updateStatus(@PathVariable @NotNull(message = "状态不能为空") Integer status, @RequestParam @NotNull(message = "id不能为空") Long id) {
        dishService.updateStatus(status, id);
        return Result.success();
    }


    @ApiOperation(value = "菜品分页查询", notes = "菜品分页查询")
    @GetMapping("/page")
    @PreAuthorize("admin:dish:page")
    public Result<PageBean> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageBean pageBean = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageBean);
    }


    @ApiOperation(value = "根据分类id查询菜品", notes = "根据分类id查询菜品")
    @GetMapping("/list")
    @PreAuthorize("admin:dish:list")
    public Result<PageBean> selectByCatId(DishPageQueryDTO dishPageQueryDTO) {
        PageBean pageBean = dishService.selectByCatId(dishPageQueryDTO);
        return Result.success(pageBean);
    }


    @ApiOperation(value = "根据id查询菜品", notes = "根据id查询菜品")
    @GetMapping("/{id}")
    @PreAuthorize("admin:dish:query")
    public Result<DishEntity> selectById(@PathVariable @NotNull(message = "id不能为空") Long id) {
        DishEntity dishEntity = dishService.selectById(id);
        return Result.success(dishEntity);
    }


    @ApiOperation(value = "新增菜品", notes = "新增菜品")
    @PostMapping
    @PreAuthorize("admin:dish:insert")
    public Result<String> insert(@RequestBody @Validated DishDTO dishDTO) {
        dishService.insert(dishDTO);
        return Result.success();
    }


    @ApiOperation(value = "批量删除菜品", notes = "批量删除菜品")
    @DeleteMapping
    @PreAuthorize("admin:dish:batchDel")
    public Result<String> batchDel(@RequestParam @NotNull(message = "id不能为空") String[] ids) {
        dishService.batchDeleteByIds(ids);
        return Result.success();
    }


    @ApiOperation(value = "修改菜品", notes = "修改菜品")
    @PutMapping
    @PreAuthorize("admin:dish:update")
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);
        return Result.success();
    }

}
