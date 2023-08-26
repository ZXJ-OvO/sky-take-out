package com.sky;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SpringDataRedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 操作字符串类型数据
     */
    @Test
    void testString() {
        redisTemplate.opsForValue().set("name", "小明");
        String city = (String) redisTemplate.opsForValue().get("name");
        System.out.println(city);

        redisTemplate.opsForValue().set("code", "1234", 3, TimeUnit.MINUTES);
        redisTemplate.opsForValue().setIfAbsent("lock", "1");
        redisTemplate.opsForValue().setIfAbsent("lock", "2");

    }

    /**
     * 操作Hash类型数据
     */
    @Test
    void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();

        hashOperations.put("100", "name", "tom");
        hashOperations.put("100", "age", "20");

        String name = (String) hashOperations.get("100", "name");
        System.out.println(name);

        Set keys = hashOperations.keys("100");
        System.out.println(keys);

        List values = hashOperations.values("100");
        System.out.println(values);

        hashOperations.delete("100", "age");
    }


}
