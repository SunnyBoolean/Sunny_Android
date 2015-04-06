/**
 * 
 */
package com.scho.note.common;

import com.scho.note.Constants;
import com.scho.note.ui.activity.UserLoginActivity;
import com.scho.note.utils.SPUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * @author 李伟
 * @Description:封装复用到得方法，比如程序中有多个地方会检查用户是否登陆以及跳转到登陆界面等
 * @date:2015年3月7日 下午2:47:56
 */

public class NoteCommon {
    
	/**
	 * 
	 * Description: 检查用户是否登陆
	 * @author: 李伟
	 * @date:2015年3月7日 下午2:49:16
	 * @return
	 */
	public static boolean checkIsUserLogined(Context context){
		if(TextUtils.isEmpty((CharSequence) SPUtils.get(context, Constants.UserInfo.USER_NAME,""))){
			return false;
		}
		if(TextUtils.isEmpty((CharSequence) SPUtils.get(context, Constants.UserInfo.USER_PASSWORD, null))){
			return false;
		}
		if(!(Boolean) SPUtils.get(context, Constants.UserInfo.IS_LOGIN,false)){
			return false; 
		}
	    return true;
	}
	/**
	 * 
	 * Description: 跳转到登陆界面
	 * @author: 李伟
	 * @date:2015年3月7日 下午2:49:52
	 */
	public static void jumpToLoginMenu(Context context){
		Intent intent = new Intent();
		intent.setClass(context, UserLoginActivity.class);
		context.startActivity(intent);
	}
}
