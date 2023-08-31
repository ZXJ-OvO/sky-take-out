package com.sky.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.*;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.EmployeeEntity;
import com.sky.exception.*;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageBean;
import com.sky.service.EmployeeService;
import com.sky.utils.IpUtil;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        // 1、只要redis中有该ip的key，并且value的值为3，就说明该ip已经被锁定了，直接抛出异常，不用继续往后走
        String ip = IpUtil.getIpAddress(httpServletRequest);
        String identifier;
        if (ip.contains(":")) {
            identifier = "IPv6-" + ip;
        } else {
            identifier = "IPv4-" + ip;
        }
        String wrongTime = ops.get(identifier);
        if (wrongTime != null && Integer.parseInt(wrongTime) >= 3) {
            throw new LoginFailedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 2、校验账号
        QueryWrapper<EmployeeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", employeeLoginDTO.getUsername());
        EmployeeEntity employeeEntity = employeeMapper.selectOne(queryWrapper);
        if (employeeEntity == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if (employeeEntity.getStatus().equals(StatusConstant.DISABLE)) {
            throw new AccountDisabledException(MessageConstant.ACCOUNT_DISABLED);
        }

        // 3、校验密码
        String dtoPwd = employeeLoginDTO.getPassword();
        String dbPwd = employeeEntity.getPassword();

        if (!BCrypt.checkpw(dtoPwd, dbPwd)) {
            // 4、密码错误，redis中的value值+1，如果value值大于3，就设置该ip的key的过期时间为30分钟
            if (wrongTime == null) {
                ops.set(identifier, "1", 1, TimeUnit.MINUTES);
            } else {
                // 5、通过设置偏移量而不是从新设置时间，保证了时间的连续性
                ops.set(identifier, String.valueOf(Integer.parseInt(wrongTime) + 1), 0);
                if (Integer.parseInt(Objects.requireNonNull(ops.get(identifier))) > 2) {
                    // 此时应该从redis拿到新的value值，而不是直接使用wrongTime
                    ops.set(identifier, "1", 30, TimeUnit.MINUTES);
                }
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
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employeeEntity.getId())
                .userName(employeeEntity.getUsername())
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

        // 1、校验用户名的唯一性，字段校验交给validator
        QueryWrapper<EmployeeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", employeeDTO.getUsername());
        EmployeeEntity dbEntity = employeeMapper.selectOne(queryWrapper);
        if (dbEntity != null) {
            throw new DuplicateFieldException(MessageConstant.DUPLICATE_USERNAME);
        }

        // 2、Hutool身份校验工具类校验身份证
        String idNumber = employeeDTO.getIdNumber();
        if (!IdcardUtil.isValidCard(idNumber)) {
            throw new InvalidFieldException("本系统仅支持中国大陆18位、15位和港澳台10位身份证号");
        }

        // 3、身份证信息脱敏
        String idCardNum = DesensitizedUtil.idCardNum(idNumber, 6, 4);
        employeeDTO.setIdNumber(idCardNum);

        // 4、对象属性拷贝 DTO -> Entity
        EmployeeEntity employeeEntity = new EmployeeEntity();
        BeanUtils.copyProperties(employeeDTO, employeeEntity);

        // 5、通过本地线程工具类获取当前登录用户的id
        Long currentId = BaseContext.getCurrentId();
        employeeEntity.setCreateUser(currentId);
        employeeEntity.setUpdateUser(currentId);

        // 6、密码加密
        String salt = BCrypt.gensalt();
        String password = BCrypt.hashpw(PasswordConstant.DEFAULT_PASSWORD, salt);

        // 7、设置默认值，createUser、updateUser、createTime、updateTime交给MyMetaObjectHandler处理
        employeeEntity.setPassword(password);
        employeeEntity.setStatus(1);

        // 8、插入数据库
        employeeMapper.insert(employeeEntity);
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO 员工分页查询DTO
     * @return 员工分页数据
     */
    @Override
    public PageBean pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {

        // 1、获取分页查询条件
        String name = employeePageQueryDTO.getName();
        int pageSize = employeePageQueryDTO.getPageSize();
        int page = employeePageQueryDTO.getPage();

        // 2、构建查询条件
        QueryWrapper<EmployeeEntity> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("update_time");

        // 3、分页查询
        Page<EmployeeEntity> pageWrapper = new Page<>(page, pageSize);
        Page<EmployeeEntity> pageData = employeeMapper.selectPage(pageWrapper, queryWrapper);
        List<EmployeeEntity> list = pageData.getRecords();

        // 4、查询总记录数
        Long total = employeeMapper.selectCount(queryWrapper);

        // 5、封装返回分页查询结果
        return PageBean.builder()
                .total(total)
                .records(list)
                .build();
    }

    /**
     * 根据id查询员工信息
     *
     * @param id 员工id not null
     * @return 员工信息
     */
    @Override
    public EmployeeEntity selectById(Long id) {
        if (id == null) {
            throw new InvalidFieldException("id不能为空");
        }
        EmployeeEntity employeeEntity = employeeMapper.selectById(id);
        if (employeeEntity == null) {
            throw new AccountNotFoundException("id为" + id + "的员工不存在");
        }
        return employeeEntity;
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        // 1、参数校验交给validator

    }
}
