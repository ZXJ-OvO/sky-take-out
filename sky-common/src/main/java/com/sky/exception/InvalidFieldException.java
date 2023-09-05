package com.sky.exception;


/**
 * 字段无效异常
 *
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/08/30 09:30
 */
public class InvalidFieldException extends BaseException {

    public InvalidFieldException() {
    }

    public InvalidFieldException(String msg) {
        super(msg);
    }
}
