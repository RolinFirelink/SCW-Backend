package com.rolin.orangesmart.annotation;

import java.lang.annotation.*;

/**
 * Author: Rolin
 * Date: 2025/3/6
 * Time: 03:46
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestInterceptor {
  int value() default 0;
}