package com.sky;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void testDeleteFile() {
        // 替换为你要删除的文件路径
        String filePath = "C:\\Users\\root\\AppData\\Local\\Temp\\tomcat.8080.1905430256023132042\\work\\Tomcat\\localhost\\ROOT\\upload_83cc6cfc_ea3d_4002_86cb_49922540897b_00000000.tmp";

        File fileToDelete = new File(filePath);

        // 测试文件是否存在，是否可写，是否删除成功
        assertTrue(fileToDelete.exists(), "文件不存在");
        assertTrue(fileToDelete.canWrite(), "文件不可写");

        // 尝试删除文件
        boolean deleted = fileToDelete.delete();

        // 断言文件删除成功
        assertTrue(deleted, "文件删除失败");
    }

}
