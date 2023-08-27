package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 * @author zxj
 */
@Data
public class UserLoginDTO implements Serializable {
    /**
     * 微信用户唯一标识：微信授权码
     */
    private String code;
}
