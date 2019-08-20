package com.weiziplus.springboot.utils;

import com.weiziplus.springboot.config.GlobalConfig;

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
        if (null == str) {
            return true;
        }
        str = str.trim();
        return 0 >= str.length() || str.equals(GlobalConfig.UNDEFINED);
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
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 对象转为字符串
     *
     * @param object
     * @return
     */
    public static String valueOf(Object object) {
        if (null == object) {
            return null;
        }
        return String.valueOf(object);
    }
}
