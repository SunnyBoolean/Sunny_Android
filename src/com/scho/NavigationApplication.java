/**
 * 
 */
package com.scho;

import java.util.HashMap;
import java.util.Map;

import com.avos.avoscloud.AVOSCloud;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

/**
 * @author:  hello
 * @Description:  TODO 
 * @date:  2015��4��16��
 */
public class NavigationApplication extends Application{
	/** Application���� */
	private static NavigationApplication instance;
    /** Ӧ������Ҫ�õ�������*/
	public static Map<Integer,Object> appData = new HashMap<Integer, Object>();
	public static final NavigationApplication getInstance() {
		if (null == instance) {
			instance = new NavigationApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        instance = this; 
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this.getApplicationContext());
        initBitMap();
        //��ʼ���Ʒ���������
        AVOSCloud.initialize(this, "6ptjoad9f4yxrcpge385h68pzvxpnsurruv48l9cj3us1ap2", "2frurfepd49xkagz8py6hb4x6bn6roue61gptmcttlhi5wbg");
	}
	/**
	 * ��ʼ������ͼƬ����
	 */
	private void initBitMap() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(config);
	}
}
