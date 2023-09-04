package com.sky.controller.user;

import com.sky.entity.CategoryEntity;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zxj
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "C端-分类接口")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @ApiOperation(value = "分类查询", notes = "根据类型查询分类")
    @GetMapping("/list")
    public Result<List<CategoryEntity>> selectByType(@RequestParam(required = false) Long type) {
        List<CategoryEntity> entities = categoryService.selectByType(type);
        return Result.success(entities);
    }
}
