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
 * @date:  2015年4月16日
 */
public class NavigationApplication extends Application{
	/** Application对象 */
	private static NavigationApplication instance;
    /** 应用中需要用到的数据*/
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
        //初始化云服务器数据
        AVOSCloud.initialize(this, "423b120v8lnmczte5ytra1pdkk59pyydushys3apgybt7xuu", "qwu4ntgotsqhoxozfss3v67pj1erdtv1skq4d279tih2soy0");
	}
	/**
	 * 初始化缓存图片配置
	 */
	private void initBitMap() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(config);
	}
}
