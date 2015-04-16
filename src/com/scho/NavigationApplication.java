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
        AVOSCloud.initialize(this, "423b120v8lnmczte5ytra1pdkk59pyydushys3apgybt7xuu", "qwu4ntgotsqhoxozfss3v67pj1erdtv1skq4d279tih2soy0");
	}
	/**
	 * ��ʼ������ͼƬ����
	 */
	private void initBitMap() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(config);
	}
}
