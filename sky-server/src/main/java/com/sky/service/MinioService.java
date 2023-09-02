package com.sky.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/09/02 14:50
 */
public interface MinioService {
    /**
     * 判断 bucket是否存在
     *
     * @param bucketName
     * @return
     */
    Boolean bucketExists(String bucketName);

    /**
     * 创建 bucket
     *
     * @param bucketName
     */
    void makeBucket(String bucketName);

    /**
     * 查询单个储桶中的所有对象
     *
     * @param bucketName
     * @return
     */
    List<Object> listObjects(String bucketName);

    /**
     * 文件上传
     *
     * @return
     */
    Boolean upload(MultipartFile multipartFile);

    void upload(MultipartFile[] multipartFile);

    /**
     * 删除文件
     */
    Boolean delFile(String bucketName, String fileName);

    /**
     * 获取文件链接
     */
    String getFileUrl(String fileName);
}
