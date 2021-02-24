package com.irappelt.mymusic.aop.annotation;

import java.lang.annotation.*;

/**
 * @author huaiyu
 * @date 2021/1/29 11:41
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExceptionCapture {
}
