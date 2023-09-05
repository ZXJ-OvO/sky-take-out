package com.sky.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String openid; //微信用户唯一标识
    private String name;    //姓名
    private String phone;    //手机号
    private String sex;    //性别 0 女 1 男
    private String idNumber;    //身份证号
    private String avatar;    //头像
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;    //注册时间
}
