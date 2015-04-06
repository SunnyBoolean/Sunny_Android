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
 * @author ��ΰ
 * @Description:��װ���õ��÷���������������ж���ط������û��Ƿ��½�Լ���ת����½�����
 * @date:2015��3��7�� ����2:47:56
 */

public class NoteCommon {
    
	/**
	 * 
	 * Description: ����û��Ƿ��½
	 * @author: ��ΰ
	 * @date:2015��3��7�� ����2:49:16
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
	 * Description: ��ת����½����
	 * @author: ��ΰ
	 * @date:2015��3��7�� ����2:49:52
	 */
	public static void jumpToLoginMenu(Context context){
		Intent intent = new Intent();
		intent.setClass(context, UserLoginActivity.class);
		context.startActivity(intent);
	}
}
