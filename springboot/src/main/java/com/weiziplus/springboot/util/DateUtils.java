package com.weiziplus.springboot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author wanglongwei
 * @date 2019/5/7 17:44
 */
public class DateUtils {

    /**
     * 时间字符串转时间
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String date, String pattern) throws ParseException {
        if (ToolUtils.isBlank(date)) {
            return null;
        }
        if (ToolUtils.isBlank(pattern)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
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
        if (ToolUtils.isBlank(date)) {
            return null;
        }
        String basePattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(basePattern.substring(0, date.length()));
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
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
        if (null == date) {
            return null;
        }
        if (ToolUtils.isBlank(pattern)) {
            return null;
        }
        //设置时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(date);
    }

    /**
     * 时间转时间字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        if (null == date) {
            return null;
        }
        //设置时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDateTime() {
        //设置时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getNowDate() {
        //设置时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(new Date());
    }
}
