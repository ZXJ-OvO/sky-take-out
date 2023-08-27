package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zxj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("employee") // 员工信息表
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户名 unique not null
     */
    private String username;
    /**
     * 姓名 not null
     */
    private String name;
    /**
     * 密码 not null
     */
    private String password;
    /**
     * 手机号 not null
     */
    private String phone;
    /**
     * 性别 not null
     */
    private String sex;
    /**
     * 身份证号 not null
     */
    private String idNumber;
    /**
     * 状态 0:禁用，1:启用 not null
     */
    private Integer status;
    /**
     * 创建时间 null
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间 null
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
    /**
     * 创建人 null
     */
    @TableField(value = "create_user")
    private Long createUser;
    /**
     * 修改人 null
     */
    @TableField(value = "update_user")
    private Long updateUser;
}
