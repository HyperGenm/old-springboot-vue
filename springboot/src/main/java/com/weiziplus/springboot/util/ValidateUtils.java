package com.weiziplus.springboot.util;

import java.util.regex.Pattern;

/**
 * 验证
 *
 * @author wanglongwei
 * @date 2019/5/9 10:08
 */
public class ValidateUtils {

    /**
     * 设置基础验证
     *
     * @param regex
     * @param string
     * @return
     */
    public static boolean baseValidate(String regex, String string) {
        if (ToolUtils.isBlank(string)) {
            return false;
        }
        return Pattern.matches(regex, string);
    }

    /**
     * 手机号
     *
     * @param phone
     * @return
     */
    public static boolean notPhone(String phone) {
        String regex = "^1[3456789]\\d{9}$";
        return !baseValidate(regex, phone);
    }

    /**
     * 正常格式密码:6-20位大小写和数字
     *
     * @param password
     * @return
     */
    public static boolean notPassword(String password) {
        String regex = "^[a-zA-Z0-9]{6,20}$";
        return !baseValidate(regex, password);
    }

    /**
     * 中英文开头、数字、下划线
     *
     * @param username
     * @return
     */
    public static boolean notChinaEnglishNumberUnderline(String username) {
        String regex = "^[a-zA-Z\\u4e00-\\u9fa5][a-zA-Z0-9\\u4e00-\\u9fa5_]{1,}$";
        return !baseValidate(regex, username);
    }

    /**
     * 正常格式真实姓名:中文或英文包括空格和点
     *
     * @param name
     * @return
     */
    public static boolean notRealName(String name) {
        String regex = "^([\\u4e00-\\u9fa5]{2,5}|[a-zA-Z\\.\\s]{2,})$";
        return !baseValidate(regex, name);
    }

    /**
     * 英文开头、数字、下划线
     *
     * @param string
     * @return
     */
    public static boolean notEnglishNumberUnderline(String string) {
        String regex = "^[a-zA-Z][a-zA-Z0-9_]{1,}$";
        return !baseValidate(regex, string);
    }

}
