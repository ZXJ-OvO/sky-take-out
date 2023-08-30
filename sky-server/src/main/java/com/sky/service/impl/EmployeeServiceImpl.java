package com.sky.service.impl;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.RedisConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.EmployeeEntity;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.InvalidFieldException;
import com.sky.exception.LoginFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zxj
 */
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO   员工信息DTO
     * @param httpServletRequest httpServletRequest
     * @return 员工信息VO
     */
    @Override
    @Transactional
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO, HttpServletRequest httpServletRequest) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        // TODO: 2023/8/27 期望：用户密码输入3次，锁定账号一个小时  实际：由于ipv4、6的缘故，无法合并统计，因此试错次数是2的倍数
        // 思路：同ip下失败3次直接禁止该ip的登录操作1小时，即锁定账号的操作和IP绑定而不是和账号绑定，防止恶意试错误封他人账号
        // 如果客户端采用了代理ip，直接放行，因为代理服务商提供的ip数量有限，并且下方的工具类中已经尽可能的考虑了代理ip的问题

        // 1、只要redis中有该ip的key，并且value的值为3，就说明该ip已经被锁定了，直接抛出异常，不用继续往后走
        String clientIP = ServletUtil.getClientIP(httpServletRequest);
        String identifier;
        if (clientIP.contains(":")) {
            identifier = "IPv6-" + clientIP;
        } else {
            identifier = "IPv4-" + clientIP;
        }
        String wrongTime = ops.get(identifier);
        if (wrongTime != null && Integer.parseInt(wrongTime) >= 3) {
            throw new LoginFailedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 2、ip不存在或者ip的value值不为3，校验参数
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        if (username == null || username.isEmpty()) {
            throw new LoginFailedException(MessageConstant.NULL_USERNAME_ERROR);
        }
        if (password == null || password.isEmpty()) {
            throw new LoginFailedException(MessageConstant.NULL_PASSWORD_ERROR);
        }

        // 3、校验账号
        QueryWrapper<EmployeeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        EmployeeEntity employeeEntity = employeeMapper.selectOne(queryWrapper);
        if (employeeEntity == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 4、校验密码
        String dtoPwd = employeeLoginDTO.getPassword();
        // 表中password的原字段是varchar，长度为64，已修改为128
        String dbPwd = employeeEntity.getPassword();
        if (!BCrypt.checkpw(dtoPwd, dbPwd)) {
            // 5、密码错误，redis中的value值+2，如果value值大于3，就设置该ip的key的过期时间为30分钟
            if (wrongTime == null) {
                ops.set(identifier, "1");
                stringRedisTemplate.expire(identifier, 30, TimeUnit.MINUTES);
            } else if (Integer.parseInt(wrongTime) <= 2) {
                ops.increment(identifier);
                ops.increment(identifier);
                stringRedisTemplate.expire(identifier, 30, TimeUnit.MINUTES);
            } else {
                stringRedisTemplate.expire(identifier, 30, TimeUnit.MINUTES);
            }
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 6、JWT生成token，token存入redis
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employeeEntity.getId());
        claims.put(JwtClaimsConstant.USERNAME, employeeEntity.getUsername());
        claims.put(JwtClaimsConstant.NAME, employeeEntity.getName());
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);

        // 7、校验通过，给VO封装数据
        EmployeeLoginVO employeeLoginVO = new EmployeeLoginVO().builder()
                .id(employeeEntity.getId())
                .userName(username)
                .name(employeeEntity.getName())
                .token(token)
                .build();

        // 8、设置redis缓存
        String tokenValue = JSONUtil.toJsonStr(employeeLoginVO);
        String tokenKey = RedisConstant.LOGIN_USER_KEY + token;
        long ttl = RandomUtil.randomLong(-5, 5) + RedisConstant.LOGIN_USER_TTL;
        ops.set(tokenKey, tokenValue);
        stringRedisTemplate.expire(tokenKey, ttl, TimeUnit.MINUTES);

        // 9、返回员工信息VO
        return employeeLoginVO;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 员工信息DTO
     */
    @Override
    @Transactional
    public void insert(EmployeeDTO employeeDTO) {

        // 1、Hutool身份校验工具类校验身份证，其他字段交给validator校验
        String idNumber = employeeDTO.getIdNumber();
        if (!IdcardUtil.isValidCard(idNumber)) {
            throw new InvalidFieldException("本系统仅支持中国大陆18位、15位和港澳台10位身份证号");
        }

        // 2、对象属性拷贝 DTO -> Entity
        EmployeeEntity employeeEntity = new EmployeeEntity();
        BeanUtils.copyProperties(employeeDTO, employeeEntity);

        // 3、密码加密，Hutool封装了BCrypt，不需要再引入相关依赖
        String salt = BCrypt.gensalt();
        String password = BCrypt.hashpw(PasswordConstant.DEFAULT_PASSWORD, salt);

        // 4、设置默认值，createUser、updateUser、createTime、updateTime交给MyMetaObjectHandler处理
        employeeEntity.setPassword(password);
        employeeEntity.setStatus(1);

        // 5、插入数据库
        employeeMapper.insert(employeeEntity);
    }
}
