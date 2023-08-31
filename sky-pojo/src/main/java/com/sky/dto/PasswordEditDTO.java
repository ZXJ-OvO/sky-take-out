package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zxj
 */
@Data
public class PasswordEditDTO implements Serializable {
    private Long empId; //员工id
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;    //旧密码
    @NotNull(message = "新密码不能为空")
    private String newPassword;    //新密码
}
