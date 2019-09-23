package com.weiziplus.springboot.base;

import java.lang.annotation.*;

/**
 * 自定义注解，数据库表中字段
 *
 * @author wanglongwei
 * @date 2019/5/17 16:52
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String value();
}