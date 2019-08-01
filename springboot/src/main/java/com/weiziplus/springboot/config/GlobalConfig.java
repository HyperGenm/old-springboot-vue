package com.weiziplus.springboot.config;

import org.springframework.beans.factory.annotation.Value;
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
    public final static String TOKEN = "token";

    /**
     * 空字符串
     */
    public static final String UNDEFINED = "undefined";

    /**
     * 用户允许禁止登录
     */
    public static final Integer ALLOW_LOGIN_ONE = 1;

    /**
     * 用户允许封号中
     */
    public static final Integer ALLOW_LOGIN_TWO = 2;

    /**
     * 允许使用为0，禁止为1
     */
    public static final Integer IS_STOP = 0;

    /**
     * 超级管理员id为1
     */
    public static final Long SUPER_ADMIN_ID = 1L;

    /**
     * 超级管理员角色id为1
     */
    public static final Long SUPER_ADMIN_ROLE_ID = 1L;

    /**
     * 系统功能表中角色管理id为3
     */
    public static final Long SYS_FUNCTION_ROLE_ID = 3L;

    /**
     * 设置可以跨域访问的地址
     */
    public static String CORS_FILTER_ORIGINS;

    @Value("${global.cors-filter-origins}")
    public void setCorsFilterOrigins(String corsFilterOrigins) {
        GlobalConfig.CORS_FILTER_ORIGINS = corsFilterOrigins;
    }

    /**
     * 设置图片上传基础路径
     */
    public static String BASE_FILE_PATH;

    @Value("${global.base-file-path}")
    public void setBaseFilePath(String baseFilePath) {
        GlobalConfig.BASE_FILE_PATH = baseFilePath;
    }
}
