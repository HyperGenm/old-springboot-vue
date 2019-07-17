package com.weiziplus.springboot.utils;

import java.text.ParseException;
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
     * 时间字符串转时间
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String date, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    /**
     * 时间字符串转时间
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        return sdf.parse(date);
    }

    /**
     * 时间转时间字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        //设置时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 时间转时间字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        //设置时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        return dateFormat.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDate() {
        //设置时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        return dateFormat.format(new Date());
    }
}
