package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zxj
 */
@RestController
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    @Resource
    private ShopService shopService;

    @GetMapping("/status")
    public Result<Integer> getStatus() {
        return Result.success(shopService.getStatus());
    }

    @PutMapping("/{status}")
    public Result<String> setStatus(@PathVariable Integer status) {
        shopService.setStatus(status);
        return Result.success();
    }
}

