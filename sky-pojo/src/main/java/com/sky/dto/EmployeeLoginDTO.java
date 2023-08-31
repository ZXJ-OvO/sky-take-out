package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zxj
 */
@Data
@ApiModel(description = "员工登录时传递的数据模型")
public class EmployeeLoginDTO implements Serializable {

    @ApiModelProperty("用户名")
    @Length(min = 4, max = 32, message = "用户名长度必须在4~32位之间")
    private String username;
    @ApiModelProperty("密码")
    @NotNull(message = "密码不能为空")
    private String password;
}
