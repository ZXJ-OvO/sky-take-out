package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zxj
 */
@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.sky.mapper")
@EnableCaching // SpringCache开启缓存
@EnableScheduling // Spring Task 开启任务调度
public class SkyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyApplication.class, args);
        log.info("项目启动成功！接口文档地址：{}", "http://localhost:8080/doc.html");
    }
}
