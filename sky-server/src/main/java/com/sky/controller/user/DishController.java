package com.sky.controller.user;

import com.sky.entity.DishEntity;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {

    @Resource
    private DishService dishService;

    @GetMapping("/list")
    public Result<List<DishEntity>> selectByType(Long categoryId) {
        List<DishEntity> entities = dishService.selectByCategoryId(categoryId);
        return Result.success(entities);
    }

}
