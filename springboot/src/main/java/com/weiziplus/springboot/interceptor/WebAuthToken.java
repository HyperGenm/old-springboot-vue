package com.weiziplus.springboot.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解webAuthToken，web端用户token
 *
 * @author wanglongwei
 * @date 2019/5/8 9:03
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebAuthToken {
}
