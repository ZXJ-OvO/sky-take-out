package com.sky.controller.user;

import com.sky.entity.DishEntity;
import com.sky.entity.SetmealEntity;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐浏览接口")
@Slf4j
public class SetmealController {


    @Resource
    private SetmealService setmealService;

    @GetMapping("/list")
    public Result<List<SetmealEntity>> selectByType(Long categoryId) {
        List<SetmealEntity> entities = setmealService.selectByCategoryId(categoryId);
        return Result.success(entities);
    }
}
