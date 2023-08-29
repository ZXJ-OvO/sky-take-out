package com.sky.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

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
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return new String(salt);
    }

    public String hashPassword(String password, String salt) {
        String passwordWithSalt = salt + password;
        return DigestUtils.sha256Hex(passwordWithSalt.getBytes());
    }
}
