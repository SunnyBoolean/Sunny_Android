package com.scho.utils;

import java.util.List;

import dalvik.system.DexClassLoader;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 
 * @author liwei
 * @Description: 应用程序辅助类，如 获取应用程序名称、获取应用程序版本号等
 * @date:2015年2月3日 上午10:00:12
 */
public class AppUtils {
	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}
    /**
     * 
     * Description: 实现动态加载，即Android插件化
     * @author: liwei
     * @date:2015年2月3日 下午2:32:33
     * @param dexPath
     * @param optimizedDirectory
     * @param libraryPath
     * @param parentClassLoader
     */
	public void initPlugIn(String dexPath,String optimizedDirectory,String libraryPath,ClassLoader parentClassLoader) {
		DexClassLoader pluginClassLoader = new DexClassLoader(dexPath,
				optimizedDirectory, libraryPath, parentClassLoader);
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * Description: 判断应用是否在后台运行，即是否按home键退出当前界面了
	 * 
	 * @author: liwei
	 * @date:2015年2月3日 下午12:12:46
	 * @param context
	 * @return
	 */
	public static boolean isApplicationInBackground(Context context) {
		// 取得一个ActivityManager对象
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 返回当前正在执行的任务列表，the most recent being first and older ones after in
		// order.
		// 这里需要获取当前的任务，因此getRuntimeTasks()参数为1，最靠前的任务
		List<RunningTaskInfo> taskList = am.getRunningTasks(1);
		if (taskList != null && !taskList.isEmpty()) {
			// 获取顶层Activit控件名字
			// CompontName意义：Identifier for a specific application component
			ComponentName topActivity = taskList.get(0).topActivity;
			if (topActivity != null
					&& !topActivity.getPackageName().equals(
							context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

}
