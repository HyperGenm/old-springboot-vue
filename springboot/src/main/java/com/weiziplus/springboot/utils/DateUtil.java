package com.weiziplus.springboot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author wanglongwei
 * @data 2019/5/7 17:44
 */
public class DateUtil {
    /**
     * 时间格式
     */
    private final static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间
     *
     * @param pattern
     * @return
     */
    public static String getDate(String pattern) {
        //设置时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        return dateFormat.format(new Date());
    }

    /**
     * 获取当前时间默认时间格式
     *
     * @return
     */
    public static String getDate() {
        return getDate(yyyy_MM_dd_HH_mm_ss);
    }
}
