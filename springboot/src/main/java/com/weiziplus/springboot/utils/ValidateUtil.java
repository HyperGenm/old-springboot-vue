package com.weiziplus.springboot.utils;

import java.util.regex.Pattern;

/**
 * 验证
 *
 * @author wanglongwei
 * @data 2019/5/9 10:08
 */
public class ValidateUtil {

    /**
     * 设置基础验证
     *
     * @param regex
     * @param string
     * @return
     */
    public static boolean baseValidate(String regex, String string) {
        if (StringUtil.isBlank(string)) {
            return false;
        }
        return Pattern.matches(regex, string);
    }

    /**
     * 判断是否是手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        String regex = "^1[3456789]\\d{9}$";
        return baseValidate(regex, phone);
    }

    /**
     * 不是手机号
     *
     * @param phone
     * @return
     */
    public static boolean notPhone(String phone) {
        return !isPhone(phone);
    }

    /**
     * 正常格式密码:6-20位大小写和数字
     *
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
        String regex = "^[a-zA-Z0-9]{6,20}$";
        return baseValidate(regex, password);
    }

    /**
     * 不是正常格式密码:6-20位大小写和数字
     *
     * @param password
     * @return
     */
    public static boolean notPassword(String password) {
        return !isPassword(password);
    }

    /**
     * 正常格式用户名:2-30位--中英文开头
     *
     * @param username
     * @return
     */
    public static boolean isUsername(String username) {
        String regex = "^[a-zA-Z\\u4e00-\\u9fa5][a-zA-Z0-9\\u4e00-\\u9fa5_]{1,30}$";
        return baseValidate(regex, username);
    }

    /**
     * 不正常格式用户名:2-30位--中英文开头
     *
     * @param username
     * @return
     */
    public static boolean notUsername(String username) {
        return !isUsername(username);
    }

    /**
     * 正常格式真实姓名:中文或英文包括空格和点
     *
     * @param name
     * @return
     */
    public static boolean isRealName(String name) {
        String regex = "^([\\u4e00-\\u9fa5]{2,5}|[a-zA-Z\\.\\s]{2,})$";
        return baseValidate(regex, name);
    }

    /**
     * 不正常格式真实姓名:2-30位--中英文开头
     *
     * @param name
     * @return
     */
    public static boolean notRealName(String name) {
        return !isRealName(name);
    }

    /**
     * 英文开头
     *
     * @param string
     * @return
     */
    public static boolean isEnglishNumberUnderline(String string) {
        String regex = "^[a-zA-Z][a-zA-Z0-9_]{1,}$";
        return baseValidate(regex, string);
    }

    /**
     * 不是英文开头
     *
     * @param string
     * @return
     */
    public static boolean notEnglishNumberUnderLine(String string) {
        return !isEnglishNumberUnderline(string);
    }

    /**
     * 邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return baseValidate(regex, email);
    }

    /**
     * 不是邮箱
     *
     * @param email
     * @return
     */
    public static boolean notEmail(String email) {
        return !isEmail(email);
    }
}
