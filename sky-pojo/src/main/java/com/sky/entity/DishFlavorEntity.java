package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品口味
 * @author zxj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("dish_flavor")
public class DishFlavorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dishId;//菜品id
    private String name;    //口味名称
    private String value;    //口味数据list
}
