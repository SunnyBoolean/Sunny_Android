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
        AVOSCloud.initialize(this, "6lkugskqcqi2mxc7ye0et5muo2agkar4bzm163utzom41p6u", "3vu7wkg9jkexdzyigftxln6xczvl973xrgp0xi0pa4669coj");
	}
	private void initBitMap() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(config);
	}
}
