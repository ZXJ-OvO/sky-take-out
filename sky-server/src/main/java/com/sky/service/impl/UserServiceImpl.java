package com.sky.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.RedisConstant;
import com.sky.constant.WeChatConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.UserEntity;
import com.sky.exception.InvalidOpenIdException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zxj
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private WeChatProperties weChatProperties;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 用户登录信息
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 1、调用微信登录接口，获取用户的唯一标识openid
        HashMap<String, String> paraMap = new HashMap<>();
        paraMap.put("appid", weChatProperties.getAppid());
        paraMap.put("secret", weChatProperties.getSecret());
        paraMap.put("js_code", userLoginDTO.getCode());
        paraMap.put("grant_type", "authorization_code");
        String body = HttpClientUtil.doGet(WeChatConstant.WX_LOGIN, paraMap);
        String openid = (String) JSON.parseObject(body).get("openid");

        if (openid == null) {
            throw new InvalidOpenIdException("微信OpenId无效异常");
        }
        log.info("openid:{}", openid);

        // 2、根据openid查询数据库，判断用户是否存在
        LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getOpenid, openid);
        UserEntity userEntity = userMapper.selectOne(lambdaQueryWrapper);
        if (userEntity == null) {
            userEntity = UserEntity.builder()
                    .openid(openid)
                    .build();
            userMapper.insert(userEntity);
        }

        // 3、生成token，将token存入redis
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, userEntity.getId());

        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .openid(openid)
                .token(token)
                .id(userEntity.getId())
                .build();

        String tokenValue = JSONUtil.toJsonStr(userLoginVO);
        String tokenKey = RedisConstant.LOGIN_USER_KEY + token;
        long ttl = RandomUtil.randomLong(-5, 5) + RedisConstant.LOGIN_USER_TTL;

        redisTemplate.opsForValue().set(tokenKey, tokenValue, ttl, TimeUnit.MINUTES);

        return userLoginVO;
    }
}
