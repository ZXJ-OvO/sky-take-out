package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

/**
 * @author zxj
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 用户登录信息
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);
}
