package com.sky.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Data
public class SetmealPageQueryDTO implements Serializable {

    @ApiModelProperty(value = "页码")
    @Range(min = 1, max = 999, message = "页码必须在1~999之间")
    private int page;
    @ApiModelProperty(value = "每页记录数")
    @Range(min = 1, max = 999, message = "每页记录数必须在1~999之间")
    private int pageSize;
    private String name;
    private Integer categoryId;    //分类id
    private Integer status;    //状态 0表示禁用 1表示启用
}
