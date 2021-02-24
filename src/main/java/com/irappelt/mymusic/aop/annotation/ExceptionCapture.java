package com.irappelt.mymusic.aop.annotation;

import java.lang.annotation.*;

/**
 * @author xiangbei
 * @date 2020/9/24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExceptionCapture {
}
