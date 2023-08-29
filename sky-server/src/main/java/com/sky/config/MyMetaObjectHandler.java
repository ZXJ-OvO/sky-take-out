package com.sky.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mybatis plus 处理公共字段的更新操作
 *
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/08/29 18:02
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 使用mp实现添加操作,这个方法会执行,metaObject元数据(表中的名字,表中的字段)
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("createUser", 1001L, metaObject);
        this.setFieldValByName("updateUser", 1001L, metaObject);

    }

    /**
     * 使用mp实现修改操作,这个方法会执行
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateUser", 1001L, metaObject);
    }
}
