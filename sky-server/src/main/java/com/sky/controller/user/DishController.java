package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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

    /**
     * 根据菜品分类id查询菜品
     *
     * @param categoryId 菜品/套餐分类id
     * @return 根据分类id查出来的分类下的所有菜品/套餐数据
     */
    @GetMapping("/list")
    public Result<List<DishVO>> selectByCategoryId(Long categoryId) {
        return Result.success(dishService.selectByCategoryId(categoryId));
    }

}
