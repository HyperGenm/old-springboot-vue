package com.weiziplus.springboot.common.config;

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
    public static final Integer SUPER_ADMIN_ROLE_ID = 1;

    /**
     * mybatis全局变量，文件路径域名前缀
     */
    private static String MYBATIS_FILE_PATH_PREFIX = "";

    @Value("${mybatis.configuration-properties.filePathPrefix:http://127.0.0.1:8080}")
    private void setMybatisFilePathPrefix(String mybatisFilePathPrefix) {
        GlobalConfig.MYBATIS_FILE_PATH_PREFIX = mybatisFilePathPrefix;
    }

    /**
     * mybatis全局变量，文件路径域名前缀
     * 只暴露属性值，不提供修改属性的方式
     *
     * @return
     */
    public static String getMybatisFilePathPrefix() {
        return MYBATIS_FILE_PATH_PREFIX;
    }

    /**
     * 设置图片上传基础路径
     */
    private static String BASE_FILE_PATH = "";

    @Value("${global.base-file-path}")
    private void setBaseFilePath(String baseFilePath) {
        GlobalConfig.BASE_FILE_PATH = baseFilePath;
    }

    /**
     * 图片上传基础路径
     * 只暴露属性值，不提供修改属性的方式
     *
     * @return
     */
    public static String getBaseFilePath() {
        return BASE_FILE_PATH;
    }

    /**
     * 当前是dev还是pro
     */
    private static String SPRING_PROFILES = "";

    @Value("${spring.profiles:pro}")
    private void setSpringProfiles(String springProfiles) {
        GlobalConfig.SPRING_PROFILES = springProfiles;
    }

    /**
     * 当前是pro
     *
     * @return
     */
    public static boolean isSpringProfilesPro() {
        return "pro".equals(SPRING_PROFILES);
    }

}
