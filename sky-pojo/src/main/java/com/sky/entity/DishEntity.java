package com.sky.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品
 * @author zxj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("dish")
@ApiModel(description = "菜品")
public class DishEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 not null unique
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 菜品名称 not null unique
     */
    @ApiModelProperty(value = "菜品名称")
    private String name;
    /**
     * 菜品分类id not null
     */
    @ApiModelProperty(value = "菜品分类id")
    private Long categoryId;
    /**
     * 菜品价格 null
     */
    @ApiModelProperty(value = "菜品价格")
    private BigDecimal price;
    /**
     * 图片 null
     */
    @ApiModelProperty(value = "图片")
    private String image;
    /**
     * 描述信息 null
     */
    @ApiModelProperty(value = "描述信息")
    private String description;
    /**
     * 状态 0 停售 1 起售 not null
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 创建时间 not null
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间 not null
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE, value = "update_time")
    private LocalDateTime updateTime;
    /**
     * 创建人 not null
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT, value = "create_user")
    private Long createUser;
    /**
     * 更新人 not null
     */
    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.UPDATE, value = "update_user")
    private Long updateUser;
}
