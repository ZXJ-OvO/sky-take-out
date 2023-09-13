package com.sky.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 编写Redis配置类，设置RedisTemplate的序列化器
 *
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/08/26 14:47
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建redisTemplate模板对象
        RedisTemplate redisTemplate = new RedisTemplate();

        // 设置redisTemplate的连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置redis key-value的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
