package com.weiziplus.springboot.common.base;

import java.lang.annotation.*;

/**
 * @author wanglongwei
 * @data 2019/5/17 16:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String value();
}