package com.scho.note.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * @author liwei
 * @Description:应用中获取时间的工具类，包含各种时间格式 
 * @date:2015年2月3日 下午1:13:57
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

   /**
    * 
    * Description: 将long类型时间格式转换成Stirng类型
    * @author: liwei
    * @date:2015年2月3日 下午1:16:53
    * @param timeInMillis
    * @return
    */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 
     * Description: 获取当前系统时间，精确到毫秒
     * @author: liwei
     * @date:2015年2月3日 下午1:15:50
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * 
     * Description: 将当前系统时间转换成String类型
     * @author: liwei
     * @date:2015年2月3日 下午1:16:18
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * 
     * Description: 获取系统时间，按照自己提供的格式化格式
     * @author: liwei
     * @date:2015年2月3日 下午1:17:50
     * @param dateFormat   时间格式
     * @return  字符串时间
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
}
