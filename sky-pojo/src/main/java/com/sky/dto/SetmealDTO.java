package com.sky.dto;

import com.sky.entity.SetmealDishEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zxj
 */
@Data
public class SetmealDTO implements Serializable {

    private Long id;
    @NotNull(message = "分类id不能为null")
    private Long categoryId;    //分类id
    @NotNull(message = "套餐名称不能为null")
    private String name;    //套餐名称
    @NotNull(message = "套餐价格不能为null")
    private BigDecimal price;    //套餐价格
    private Integer status;    //状态 0:停用 1:启用
    private String description;    //描述信息
    @NotNull(message = "套餐图片不能为null")
    private String image;    //图片
    @NotNull(message = "套餐菜品关系不能为null")
    private List<SetmealDishEntity> setmealDishes = new ArrayList<>();    //套餐菜品关系
}
