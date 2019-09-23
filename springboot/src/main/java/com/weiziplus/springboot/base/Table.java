package com.weiziplus.springboot.base;

import java.lang.annotation.*;

/**
 * 自定义注解，数据库表名
 *
 * @author wanglongwei
 * @date 2019/5/17 16:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String value();
}