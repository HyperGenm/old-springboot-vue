package com.weiziplus.springboot.common.utils;

import com.weiziplus.springboot.common.config.GlobalConfig;

import java.util.UUID;

/**
 * 字符串常用工具
 *
 * @author wanglongwei
 * @data 2019/5/7 17:00
 */
public class StringUtil {
    /**
     * 字符串为null或""或"undefined"
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return null == str || 0 >= str.length() || str.equals(GlobalConfig.UNDEFINED);
    }

    /**
     * 字符串不为null或""或"undefined"
     *
     * @param str
     * @return
     */
    public static boolean notBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 生成uuid
     *
     * @return
     */
    public static String createUUID() {
        return UUID.randomUUID().toString();
    }
}
