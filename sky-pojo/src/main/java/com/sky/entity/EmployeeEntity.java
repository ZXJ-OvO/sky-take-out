package com.sky.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zxj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "员工信息表")
@TableName("employee")
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 not null unique
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 用户名 unique not null
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 姓名 not null
     */
    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 密码 not null
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 手机号 not null
     */
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**
     * 性别 not null
     */
    @ApiModelProperty(value = "性别")
    private String sex;
    /**
     * 身份证号 not null
     */
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
    /**
     * 状态 0:禁用，1:启用 not null
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 创建时间 null
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间 null
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
    /**
     * 创建人 null
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT, value = "create_user")
    private Long createUser;
    /**
     * 修改人 null
     */
    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_user")
    private Long updateUser;
}
