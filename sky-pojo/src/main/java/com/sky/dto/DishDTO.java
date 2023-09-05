package com.sky.dto;

import com.sky.entity.DishFlavorEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zxj
 */
@Data
@ApiModel(description = "菜品信息DTO")
public class DishDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "菜品名称")
    @Length(min = 2, max = 32, message = "菜品名称长度必须在2~32位之间")
    private String name;

    @ApiModelProperty(value = "菜品分类id")
    @Range(min = 1, max = 999, message = "菜品分类id必须在1~999之间")
    private Long categoryId;

    @ApiModelProperty(value = "菜品价格")
    @DecimalMin(value = "0.01", message = "菜品价格不能小于0.01")
    private BigDecimal price;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "描述信息")
    private String description;

    @ApiModelProperty(value = "0 停售 1 起售")
    private Integer status;

    @ApiModelProperty(value = "口味")
    private List<DishFlavorEntity> flavors = new ArrayList<>();
}
