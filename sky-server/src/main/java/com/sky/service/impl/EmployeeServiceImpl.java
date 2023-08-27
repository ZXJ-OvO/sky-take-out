package com.sky.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.EmployeeEntity;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.LoginFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author zxj
 */
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 员工登录
     *
     * @param employeeLoginDTO 员工信息DTO
     * @return 员工信息VO
     */
    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) {
        // TODO: 2023/8/27 校验参数后期采用其他方式，如JSR303
        // 1、校验参数
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        if (username == null || username.isEmpty()) {
            throw new LoginFailedException(MessageConstant.NULL_USERNAME_ERROR);
        }
        if (password == null || password.isEmpty()) {
            throw new LoginFailedException(MessageConstant.NULL_PASSWORD_ERROR);
        }

        // 2、校验账号
        QueryWrapper<EmployeeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        EmployeeEntity employeeEntity = employeeMapper.selectOne(queryWrapper);
        if (employeeEntity == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 3、校验密码
        String dtoPwd = DigestUtils.md5DigestAsHex(password.getBytes());
        String dbPwd = employeeEntity.getPassword();
        if (!dbPwd.equals(dtoPwd)) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 4、校验通过，返回员工信息VO
        EmployeeLoginVO employeeLoginVO = new EmployeeLoginVO();
        employeeLoginVO.setId(employeeEntity.getId());
        employeeLoginVO.setUserName(username);
        employeeLoginVO.setName(employeeEntity.getName());
        // TODO: 2023/8/27 JWT生成token，token存入redis
        String token = UUID.randomUUID().toString();
        employeeLoginVO.setToken(token);
        String voJsonStr = JSONUtil.toJsonStr(employeeLoginVO);
        log.info("员工信息Json：{}", voJsonStr);
        /*
            {
                "id": 1,
                "userName": "admin",
                "name": "管理员",
                "token": "05f02531-e8f3-4968-aa89-fb90f00031d2"
            }
         */
        redisTemplate.opsForValue().set(token, voJsonStr);

        // 5、返回员工信息VO
        return employeeLoginVO;
    }
}
