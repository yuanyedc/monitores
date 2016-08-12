package com.msds.monitor.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 功能:通用的静态工具函数(用于日期和时间处理)
 * <p>Title: 宝易互通支付平台</p>
 * <p>Description: 通用工具类</p>
 */
public class DateTimeUtils {

    public final static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String DATETIME_FORMAT_HHMMSS = "HHmmss";
    public final static String DATETIME_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String DATETIME_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String DATETIME_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public final static String DATETIME_FORMAT_YYYYMMDDHH = "yyyyMMddHH";
    public final static String DATETIME_FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public final static String DATETIME_FORMAT_YYYY = "yyyy";
    public final static String DATETIME_FORMAT_YYYY_MM_DD_CN = "yyyy年MM月dd日";
    public final static String DATETIME_FORMAT_HH_MM_SS = "HH:mm:ss";
    public final static String DATE_FORMAT_YYYYPOINTMMPOINTDD = "yyyy.MM.dd";
    /**
     * AOP Date默认时区
     **/
    public static final String DATE_TIMEZONE = "GMT+8";


    /**
     * 获得系统当前时间
     *
     * @return Date
     */
    public static Date getNowDate() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    /**
     * 把日期按照指定格式的转化成字符串
     *
     * @param date      日期对象
     * @param formatStr 日期格式
     * @return 字符串式的日期, 格式为：yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeToString(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }
}
