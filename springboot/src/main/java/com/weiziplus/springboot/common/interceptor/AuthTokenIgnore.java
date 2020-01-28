package com.weiziplus.springboot.common.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * token忽略注解
 *
 * @author wanglongwei
 * @date 2019/6/24 8:23
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthTokenIgnore {
}
