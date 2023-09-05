package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @author zxj
 */
@Data
@ApiModel(description = "分类信息DTO")
public class CategoryDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "类型 1 菜品分类 2 套餐分类")
    private Integer type;
    @ApiModelProperty(value = "分类名称")
    @Length(min = 2, max = 32, message = "分类名称长度必须在2~32位之间")
    private String name;
    @ApiModelProperty(value = "排序")
    @Range(min = 1, max = 9999, message = "排序值必须在1~9999之间")
    private Integer sort;
}
