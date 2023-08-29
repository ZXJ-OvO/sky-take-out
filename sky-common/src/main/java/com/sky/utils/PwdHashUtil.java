package com.sky.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * 使用SHA-256对密码进行hash
 *
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/08/29 09:46
 */
@Component
public class PwdHashUtil {
    private static final int SALT_LENGTH = 16; // 盐的长度

    public String generateSalt() {
        // todo 加盐处理应该在注册的时候将随机盐作为字段数据随着密码一期保存到数据库中，因此现在无法实现
/*        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);*/
        // 暂时以固定盐代替
        byte[] salt = "e10adc3949ba59abbe56e057f20f883e".getBytes();
        return new String(salt);
    }

    public String hashPassword(String password, String salt) {
        String passwordWithSalt = salt + password;
        return DigestUtils.sha256Hex(passwordWithSalt.getBytes());
    }
}
