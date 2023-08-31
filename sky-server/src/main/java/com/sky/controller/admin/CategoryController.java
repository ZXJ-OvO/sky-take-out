package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.CategoryEntity;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类相关接口")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param categoryDTO 分类信息
     * @return Result<String>
     */
    @ApiOperation(value = "新增分类", notes = "新增分类")
    @PostMapping
    public Result<String> insert(@RequestBody @Validated CategoryDTO categoryDTO) {
        categoryService.insert(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO 分类分页查询条件
     * @return Result<PageBean>
     */
    @ApiOperation(value = "分类分页查询", notes = "分类分页查询")
    @GetMapping("/page")
    public Result<PageBean> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageBean pageBean = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageBean);
    }

    /**
     * 根据id删除分类
     *
     * @param id 分类id
     * @return Result<String>
     */
    @ApiOperation(value = "根据id删除分类", notes = "根据id删除分类")
    @DeleteMapping
    public Result<String> deleteById(@RequestParam Long id) {
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 分类查询
     *
     * @param type 类型
     * @return Result<List < CategoryEntity>>
     */
    @ApiOperation(value = "分类查询", notes = "根据类型查询分类")
    @GetMapping("/list")
    public Result<List<CategoryEntity>> selectByType(Integer type) {
        List<CategoryEntity> categoryEntities = categoryService.selectByType(type);
        return Result.success(categoryEntities);
    }

    /**
     * 分类状态
     *
     * @param status 状态
     * @param id     分类id
     * @return Result<String>
     */
    @ApiOperation(value = "分类状态", notes = "分类禁用与启用操作")
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable @NotNull(message = "状态不能为空") Integer status, @PathVariable @NotNull(message = "id不能为空") Long id) {
        categoryService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param categoryDTO 分类信息
     * @return Result<String>
     */
    @ApiOperation(value = "修改分类", notes = "修改分类")
    @PutMapping
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }
}
