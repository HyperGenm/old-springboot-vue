package com.weiziplus.springboot.util;

import com.weiziplus.springboot.config.GlobalConfig;

import java.util.UUID;

/**
 * 常用工具类
 *
 * @author wanglongwei
 * @data 2019/8/27 02:16
 */
public class ToolUtils {

    /**
     * 字符串为null或""或"undefined"或"null"
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (null == str) {
            return true;
        }
        str = str.trim();
        return 0 >= str.length() || GlobalConfig.UNDEFINED.equals(str) || GlobalConfig.NULL.equals(str);
    }

    /**
     * 字符串不是null或""或"undefined"或"null"
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
    public static String valueOfString(Object object) {
        if (null == object) {
            return null;
        }
        return String.valueOf(object);
    }

    /**
     * 字符串转Integer
     *
     * @param string
     * @return
     */
    public static Integer valueOfInteger(String string) {
        if (null == string) {
            return null;
        }
        string = string.trim();
        if (0 >= string.length() || GlobalConfig.UNDEFINED.equals(string) || GlobalConfig.NULL.equals(string)) {
            return null;
        }
        return Integer.valueOf(string);
    }

    /**
     * 字符串转Double
     *
     * @param string
     * @return
     */
    public static Double valueOfDouble(String string) {
        if (null == string) {
            return null;
        }
        string = string.trim();
        if (0 >= string.length() || GlobalConfig.UNDEFINED.equals(string) || GlobalConfig.NULL.equals(string)) {
            return null;
        }
        return Double.valueOf(string);
    }

    /**
     * 字符串转Long
     *
     * @param string
     * @return
     */
    public static Long valueOfLong(String string) {
        if (null == string) {
            return null;
        }
        string = string.trim();
        if (0 >= string.length() || GlobalConfig.UNDEFINED.equals(string) || GlobalConfig.NULL.equals(string)) {
            return null;
        }
        return Long.valueOf(string);
    }
}
