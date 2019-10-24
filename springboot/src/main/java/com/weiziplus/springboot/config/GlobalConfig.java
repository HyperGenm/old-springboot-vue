package com.weiziplus.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 设置全局静态常量
 *
 * @author wanglongwei
 * @date 2019/5/6 15:50
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

    /**
     * mybatis全局变量，文件路径域名前缀
     */
    public static String MYBATIS_FILE_PATH_PREFIX = "";

    @Value("${mybatis.configuration-properties.filePathPrefix:http://127.0.0.1:8080}")
    public void setMybatisFilePathPrefix(String prefix) {
        GlobalConfig.MYBATIS_FILE_PATH_PREFIX = prefix;
    }

    /**
     * 设置图片上传基础路径
     */
    public static String BASE_FILE_PATH;

    @Value("${global.base-file-path}")
    private void setBaseFilePath(String baseFilePath) {
        GlobalConfig.BASE_FILE_PATH = baseFilePath;
    }

}
