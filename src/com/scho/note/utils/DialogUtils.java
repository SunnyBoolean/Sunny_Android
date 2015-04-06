package com.scho.note.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.note.ui.activity.R;
/**
 * 
 * @author liwei
 * Description: �Զ���Dialog�����ֻ࣬��Ҫ׼��һ���Ѿ���ŵĲ���xml�ļ�ʹ�ô��༴������һ���Զ���Dialog 
 * @date:2015��2��3�� ����10:02:02
 */
public class DialogUtils {

	public static Dialog getBottomDialog(Context context, View contentView,
			Dialog d,Activity ac) {
		// ��ֹdialog�ظ�����
		if (d != null && d.isShowing()) {
			return d;
		}
		if(d != null){
			return d;
		}
		Dialog dialog = new Dialog(context, R.style.dialog);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
		int[] wh = getDeviceWH(context);
		float[] whs = NoteUtil.getWHofDevice(ac);
		
		windowParams.x = 0;
		windowParams.y = (int) whs[0]/50;
		//windowParams.alpha = 0.6f;
		// ����dialogͣ��λ��
		window.setAttributes(windowParams);
		// window.setBackgroundDrawableResource(R.drawable.alert_dialog_background);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(contentView);
		// ���վ���dialog�Ĵ�С,ʵ����contentViewȷ����
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
//		dialog.show();
		return dialog;
	}

	/**
	 * ��ȡ�豸�ĸߺͿ�
	 * Description: TODO
	 * @author: liwei
	 * @date:2015��2��1�� ����2:54:06
	 * @param context
	 * @return
	 */
	public static int[] getDeviceWH(Context context) {
		int[] wh = new int[2];
		int w = 0;
		int h = 0;
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		w = dm.widthPixels;
		h = dm.heightPixels;
		wh[0] = w;
		wh[1] = h;
		return wh;
	}
	
	/**
	 * 
	 * Description: ��ȡPopupWindow
	 * @author: liwei
	 * @date:2015��2��2�� ����8:55:24
	 * @param activity
	 * @return
	 */
	public static PopupWindow getPopupWindow(View view){
        PopupWindow poupWindow = null; 
        if(null != view){
        	poupWindow = new PopupWindow(view);
        }
		return poupWindow;
	}

}
