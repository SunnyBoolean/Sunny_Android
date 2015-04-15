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
 * @date:2015��2��1�� ����12:35:02
 */

public class NoteUtil {
	/**
	 * Description: �������һ��Ψһ��id
	 * @author: liwei
	 * @date:2015��2��1�� ����12:35:09
	 * @return Ψһid
	 */
	 public static String getId() {
	        return UUID.randomUUID().toString().replaceAll("-", "");
	    }
	 /**
	  * 
	  * Description: ��ȡ�ֻ��ֱ��ʣ�����͸�
	  * @author: liwei
	  * @date:2015��2��1�� ����3:28:13
	  * @return ����һ��float���飬[0]�洢�ߣ�[1]�洢��
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
	  * �������α�׼ʱ��ת��Ϊ������׼ʱ��
	  * Description: TODO
	  * @author: ��ΰ
	  * @date:2015��4��5�� ����2:03:44
	  * @param date  �������α�׼ʱ��  
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
