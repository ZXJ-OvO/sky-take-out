package com.sky.aspect;


import com.sky.annotation.PreAuthorize;
import com.sky.context.BaseContext;
import com.sky.mapper.PreAuthorizeMapper;
import com.sky.result.Result;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class PreAuthorizeAspect {
    @Resource
    private PreAuthorizeMapper preAuthorizeMapper;

    @SneakyThrows
    @Around("execution(* com.sky.controller.*.*.*(..))&&@annotation(com.sky.annotation.PreAuthorize)")
    public Object preAuthorize(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        PreAuthorize annotation = signature.getMethod().getAnnotation(PreAuthorize.class);
        String value = annotation.value();

        Long currentId = BaseContext.getCurrentId();
        // 放行超级管理员
        // 对于登录逻辑，不需要权限

        if (currentId == 1) {
            return joinPoint.proceed();
        }

        Long count = preAuthorizeMapper.selectPerms(value, currentId);

        // 有权限
        if (count != null && count == 1) {
            return joinPoint.proceed();
        }

        // 没权限
        String msg = "您没有数据的权限,前联系管理员添加权限," + "[" + value + "]";
        return Result.error(msg);
    }
}
