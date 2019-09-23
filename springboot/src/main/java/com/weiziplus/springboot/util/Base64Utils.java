package com.weiziplus.springboot.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 字符串加密解密
 *
 * @author wanglongwei
 * @date 2019/9/11 16:13
 */
public class Base64Utils {
    /**
     * 加密、解密盐
     */
    private static final String SALT = "WeiziPlus";

    /**
     * 字符串加密
     *
     * @param text
     * @return
     */
    public static String encode(String text) {
        if (null == text) {
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return encoder.encodeToString(bytes);
    }

    /**
     * 字符串解密
     *
     * @param text
     * @return
     */
    public static String decode(String text) {
        if (null == text) {
            return null;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(text);
        return new String(decode, StandardCharsets.UTF_8);
    }

}
