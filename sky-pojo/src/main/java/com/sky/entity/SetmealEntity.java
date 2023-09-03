package com.sky.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐
 * @author zxj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("setmeal")
public class SetmealEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long categoryId;    //分类id
    private String name;    //套餐名称
    private BigDecimal price;    //套餐价格
    private Integer status;    //状态 0:停用 1:启用
    private String description;    //描述信息
    private String image;    //图片
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT, value = "create_user")
    private Long createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_user")
    private Long updateUser;
}
