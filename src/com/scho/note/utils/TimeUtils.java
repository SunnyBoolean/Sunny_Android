package com.scho.note.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * @author liwei
 * @Description:Ӧ���л�ȡʱ��Ĺ����࣬��������ʱ���ʽ 
 * @date:2015��2��3�� ����1:13:57
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
    * Description: ��long����ʱ���ʽת����Stirng����
    * @author: liwei
    * @date:2015��2��3�� ����1:16:53
    * @param timeInMillis
    * @return
    */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 
     * Description: ��ȡ��ǰϵͳʱ�䣬��ȷ������
     * @author: liwei
     * @date:2015��2��3�� ����1:15:50
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * 
     * Description: ����ǰϵͳʱ��ת����String����
     * @author: liwei
     * @date:2015��2��3�� ����1:16:18
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * 
     * Description: ��ȡϵͳʱ�䣬�����Լ��ṩ�ĸ�ʽ����ʽ
     * @author: liwei
     * @date:2015��2��3�� ����1:17:50
     * @param dateFormat   ʱ���ʽ
     * @return  �ַ���ʱ��
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
}
