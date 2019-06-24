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
     * 用户允许登录为0，禁止为1
     */
    public static final Integer ALLOW_LOGIN = 0;

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
     * 设置图片上传基础路径
     */
    public static String BASE_FILE_PATH;

    @Value("${global.base-file-path}")
    public void setBaseFilePath(String baseFilePath) {
        GlobalConfig.BASE_FILE_PATH = baseFilePath;
    }
}