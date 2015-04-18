/**
 * 
 */
package com.scho;

import java.util.HashMap;
import java.util.Map;



import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class NavigationApplication extends Application{
	private static NavigationApplication instance;
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
     // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
     		SDKInitializer.initialize(this);
	}
	private void initBitMap() {
	}
}
