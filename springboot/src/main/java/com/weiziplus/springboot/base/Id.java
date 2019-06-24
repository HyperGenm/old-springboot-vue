package com.weiziplus.springboot.base;

import java.lang.annotation.*;

/**
 * 自定义注解，数据库表主键
 *
 * @author wanglongwei
 * @data 2019/5/17 16:52
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
    String value();
}