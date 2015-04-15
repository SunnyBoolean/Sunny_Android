/**
 * 
 */
package com.scho.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * @author liwei
 * @Description:TODO 
 * @date:2015年2月1日 下午12:35:02
 */

public class NoteUtil {
	/**
	 * Description: 随机产生一个唯一的id
	 * @author: liwei
	 * @date:2015年2月1日 下午12:35:09
	 * @return 唯一id
	 */
	 public static String getId() {
	        return UUID.randomUUID().toString().replaceAll("-", "");
	    }
	 /**
	  * 
	  * Description: 获取手机分辨率，即宽和高
	  * @author: liwei
	  * @date:2015年2月1日 下午3:28:13
	  * @return 返回一个float数组，[0]存储高，[1]存储宽
	  */
	 public static float[] getWHofDevice(Activity ac){
		 float[] wh = new float[2];
		 Display display = ac.getWindowManager().getDefaultDisplay(); 
		 Point size = new Point();
		 display.getSize(size);
		 wh[0] = size.y;
		 wh[1] = size.x;
		 return wh;
	 }
	 /**
	  * 格林尼治标准时间转换为北京标准时间
	  * Description: TODO
	  * @author: 李伟
	  * @date:2015年4月5日 下午2:03:44
	  * @param date  格林尼治标准时间  
	  * 
	  * @return
	  */
	 public static String gelinTimeToChinaTime(Date date){
		 String chinaTime = "";
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",new Locale("English"));
	        if(chinaTime !=null){
	        	chinaTime = format.format(date);
	        }
	        return chinaTime;
	 }

}
