package com.irappelt.mymusic.aop.exception;

/**
 * 参数校验异常
 *
 * @author huaiyu
 * @date 2021/1/29 11:41
 */
public class ParamVerifyException extends RuntimeException {

    public ParamVerifyException() {
    }

    public ParamVerifyException(String message) {
        super(message);
    }
}
