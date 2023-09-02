package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProp {

    //Minio服务所在地址
    private String endpoint;
    //访问的key
    private String accessKey;
    //访问的秘钥
    private String secretKey;
    //存储桶名称
    private String bucketName;
}
