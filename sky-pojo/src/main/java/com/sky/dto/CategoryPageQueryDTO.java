package com.sky.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @author zxj
 */
@Data
public class CategoryPageQueryDTO implements Serializable {

    @ApiModelProperty(value = "页码")
    @Range(min = 1, max = 999, message = "页码必须在1~999之间")
    private int page;       //页码
    @ApiModelProperty(value = "每页记录数")
    @Range(min = 1, max = 999, message = "每页记录数必须在1~999之间")
    private int pageSize;    //每页记录数
    @ApiModelProperty(value = "姓名")
    private String name;    //分类名称
    @ApiModelProperty(value = "分类类型 1菜品分类  2套餐分类")
    private Integer type;
}
