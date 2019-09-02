package com.weiziplus.springboot.util;

import java.nio.charset.StandardCharsets;

/**
 * 字符串加密解密
 *
 * @author wanglongwei
 * @data 2019/7/25 11:08
 */
public class CryptoUtil {

    /**
     * 加密，把一个字符串在原有的基础上+1
     *
     * @param data 需要解密的原字符串
     * @return 返回解密后的新字符串
     */
    public static String encode(String data) {
        if (null == data) {
            return null;
        }
        byte[] b = data.getBytes(StandardCharsets.UTF_8);
        //遍历
        for (int i = 0; i < b.length; i++) {
            if (i % 2 == 0) {
                b[i] += 1;
            } else {
                b[i] -= 1;
            }
        }
        return new String(b, StandardCharsets.UTF_8);
    }

    /**
     * 解密：把一个加密后的字符串在原有基础上-1
     *
     * @param data 加密后的字符串
     * @return 返回解密后的新字符串
     */
    public static String decode(String data) {
        if (null == data) {
            return null;
        }
        byte[] b = data.getBytes(StandardCharsets.UTF_8);
        //遍历
        for (int i = 0; i < b.length; i++) {
            if (i % 2 == 0) {
                b[i] -= 1;
            } else {
                b[i] += 1;
            }
        }
        return new String(b, StandardCharsets.UTF_8);
    }
}
