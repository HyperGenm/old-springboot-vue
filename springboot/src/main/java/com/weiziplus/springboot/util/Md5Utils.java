package com.weiziplus.springboot.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * MD5工具类
 *
 * @author wanglongwei
 * @date 2019/5/7 10:15
 */
public class Md5Utils {
    /**
     * MD5加密盐
     */
    private static final String SALT = "WeiziPlus";

    /**
     * MD5加密  32位小写
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        if (ToolUtils.isBlank(str)) {
            return null;
        }
        //在字符串基础上添加加密盐
        str = SALT + str;
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 任意类型 MD5加密
     *
     * @param object
     * @return
     */
    public static String encode(Object object) {
        return encode(ToolUtils.valueOfString(object));
    }
}
