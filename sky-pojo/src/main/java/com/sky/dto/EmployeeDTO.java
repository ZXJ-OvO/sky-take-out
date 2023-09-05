package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author zxj
 */
@Data
@ApiModel(description = "员工信息DTO")
public class EmployeeDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "用户名")
    @Length(min = 4, max = 32, message = "用户名长度必须在4~32位之间")
    private String username;
    @ApiModelProperty(value = "姓名")
    @Length(min = 2, max = 32, message = "姓名长度必须在2~32位之间")
    private String name;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
}
