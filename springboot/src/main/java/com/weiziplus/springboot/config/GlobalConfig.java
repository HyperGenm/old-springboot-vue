package com.weiziplus.springboot.config;

import org.springframework.stereotype.Component;

/**
 * 设置全局静态常量
 *
 * @author wanglongwei
 * @data 2019/5/6 15:50
 */
@Component
public class GlobalConfig {

    /**
     * 身份验证，请求头，token
     */
    public static final String TOKEN = "token";

    /**
     * 空字符串
     */
    public static final String UNDEFINED = "undefined";

    /**
     * 空字符串
     */
    public static final String NULL = "null";

    /**
     * 超级管理员id为1
     */
    public static final Long SUPER_ADMIN_ID = 1L;

    /**
     * 超级管理员角色id为1
     */
    public static final Long SUPER_ADMIN_ROLE_ID = 1L;

}
