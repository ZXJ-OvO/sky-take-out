package com.sky;


import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/09/03 22:44
 */
@SpringBootTest
public class HttpClientTest {

    @Test
    @SneakyThrows
    void testGet() {
        // 创建Http对象

        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建请求对象
        HttpGet httpGet = new HttpGet("https://www.baidu.com");

        // 发送请求，接收响应结果
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 获取服务端响应的状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        // 获取响应体
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println(body);

        // 关闭资源
        response.close();
        httpClient.close();
    }

    @SneakyThrows
    @Test
    void testPost() {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("https://www.baidu.com");

        // 通过fastjson设置键值对，构建一个json字符串
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "zxj");
        // 设置请求体
        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        // 设置编码
        stringEntity.setContentEncoding("UTF-8");
        // 设置请求体格式
        stringEntity.setContentType("application/json");
        // 封装到请求对象中
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println(body);

        response.close();
        httpClient.close();
    }
}
