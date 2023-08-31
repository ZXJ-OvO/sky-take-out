package com.sky.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @author zxj
 */
@Data
public class EmployeePageQueryDTO implements Serializable {

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "页码")
    @Range(min = 1, max = 999, message = "页码必须在1~999之间")
    private int page;
    @ApiModelProperty(value = "每页记录数")
    @Range(min = 1, max = 999, message = "每页记录数必须在1~999之间")
    private int pageSize;
}
