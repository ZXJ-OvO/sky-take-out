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
@ApiModel(description = "菜品及套餐分类")
@TableName("category")
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 not null unique
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 类型 1 菜品分类 2 套餐分类 not null
     */
    @ApiModelProperty(value = "类型")
    private Integer type;
    /**
     * 分类名称 not null
     */
    @ApiModelProperty(value = "分类名称")
    private String name;
    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序")
    private Integer sort;
    /**
     * 状态 1启用 0禁用
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
