package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zxj
 */
@Data
public class ShoppingCartDTO implements Serializable {

    private Long dishId;
    private Long setmealId;
    private String dishFlavor;
}
