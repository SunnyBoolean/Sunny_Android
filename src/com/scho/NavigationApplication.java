/**
 * 
 */
package com.scho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.scho.entrty.LocationInfo;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.util.Log;

public class NavigationApplication extends Application {
	/** Application实例 */
	private static NavigationApplication instance;
	/** 存储全局的数据 */
	public static Map<Integer, Object> appData = new HashMap<Integer, Object>();
	/** 定位客户端服务 */
	public LocationClient mLocationClient;
	/** 低功耗地理围栏类 */
	public GeofenceClient mGeofenceClient;
	/** 实时定位监听器 */
	public MyLocationListener mMyLocationListener;
	/** 手机震动服务*/
	public Vibrator mVibrator;
    /** 存储定位信息，供应用程序使用*/
	public ArrayList<LocationInfo> mLocationListInfo = new ArrayList<LocationInfo>(); 
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
		initSystem();
		
		initBaiduMap();
	}
	/**
	 * 初始化系统相关
	 */
    private void initSystem(){
    	//crash异常捕捉实例
    	CrashHandler crashHandler = CrashHandler.getInstance();
    	//初始化
		crashHandler.init(this.getApplicationContext());
    	//获取震动服务
    	mVibrator=(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }
	/**
	 * 初始化百度地图、定位相关
	 */
	private void initBaiduMap() {
		// 在使用 百度SDK各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		//获取定位服务
		mLocationClient = new LocationClient(this.getApplicationContext());
		//定位监听
		mMyLocationListener = new MyLocationListener();
		//注册定位
		mLocationClient.registerLocationListener(mMyLocationListener);
		//低功耗围栏获取
		mGeofenceClient = new GeofenceClient(getApplicationContext());
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			LocationInfo locationInfo = new LocationInfo();
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			//定位时间
			locationInfo.time = location.getTime();
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			//经度
			locationInfo.latitude = location.getLatitude();
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			//纬度
			locationInfo.lontitude = location.getLongitude();
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			//半径
			locationInfo.radius = location.getRadius()+"";
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				//速度
				locationInfo.speed = location.getSpeed()+"";
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				//地理位置名称
				locationInfo.address = location.getAddrStr();
				sb.append(location.getAddrStr());
				//方向
				locationInfo.direction = location.getDirection()+"";
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				locationInfo.address = location.getAddrStr();
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			//保存定位信息，为防止长时间保存大量定位信息，应该隔一段时间就清理一下mLocationListInfo
			mLocationListInfo.add(locationInfo);
			Log.i("BaiduLocationApiDem", sb.toString());
		}

	}
}
