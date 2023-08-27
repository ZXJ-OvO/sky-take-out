package com.sky.service.impl;

import com.sky.dto.UserLoginDTO;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zxj
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 用户登录信息
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        userMapper.selectById(userLoginDTO);
        return null;
    }
}
