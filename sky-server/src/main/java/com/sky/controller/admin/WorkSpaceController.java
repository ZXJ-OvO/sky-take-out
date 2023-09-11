package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "工作台相关接口")
public class WorkSpaceController {

    @Resource
    private WorkspaceService workspaceService;

    /**
     * 获取当日工作台数据
     *
     * @return BusinessDataVO
     */
    @GetMapping("/businessData")
    @ApiOperation(value = "查询今日运营数据")
    public Result<BusinessDataVO> businessData() {
        return Result.success(workspaceService.getBusinessData());
    }

    @GetMapping("/overviewOrders")
    @ApiOperation(value = "查询订单管理数据")
    public Result<OrderOverViewVO> overviewOrders() {
        return Result.success(workspaceService.getOverviewOrders());
    }

    //  /admin/workspace/overviewDishes
    @GetMapping("/overviewOrders")
    @ApiOperation(value = "查询订单管理数据")
    public Result<OrderOverViewVO> overviewOrders() {
        return Result.success(workspaceService.getOverviewOrders());
    }
}
