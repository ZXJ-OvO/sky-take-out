package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号格式不满足当前中国大陆号段要求")
    private String phone;
    @ApiModelProperty(value = "性别")
    @Pattern(regexp = "^([10])$", message = "性别仅支持'男'或'女'")
    private String sex;
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
}
