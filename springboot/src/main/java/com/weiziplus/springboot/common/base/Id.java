package com.weiziplus.springboot.common.base;

import java.lang.annotation.*;

/**
 * 自定义注解，数据库表主键
 *
 * @author wanglongwei
 * @date 2019/5/17 16:52
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
    String value();
}