package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zxj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {

    /**
     * 用户id
     */
    private Long id;
    /**
     * 微信用户openid
     */
    private String openid;
    /**
     * jwt令牌
     */
    private String token;
}
