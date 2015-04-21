package com.scho.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.scho.Constants;
import com.scho.NavigationApplication;
import com.scho.broadcast.KeyValidateReceiver;
import com.scho.entrty.LocationInfo;
import com.scho.gpsnavigation.utils.ImageTool;
import com.scho.ui.R;

public class MainActivity extends Activity {
	/** 上下文 */
	private Context mContext;
	/** 地图容器 */
	private MapView mMapView;
	/** 百度地图 */
	private BaiduMap mBaiduMap;
	/** 接受广播消息，确定key值验证是否通过 */
	private KeyValidateReceiver mKeyReceiver;
	/** 定位服务客户端 */
	private LocationClient mLocationClient;
	/** 定位模式 */
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	/** 定位时间间隔，默认每个1s定位一次 */
	private int mSpaneFrequence = 1000;
	/** 定位信息 */
	private ArrayList<LocationInfo> mLocationList;
	/** Handler对象 */
	private TaskHandler mHandler;
    /** 进度条，当用户进来时，会先进行定位然后显示定位后的地图*/
	private ProgressDialog mProgress ;
	/** NotificationManager 管理通知栏，当用户进入某景点时就会出现通知*/
	private NotificationManager mNotificationManager;
	
	/** 城市查询*/
	private TextView mCitySearch;
	/** 路线导航*/
	private TextView mRoadNavigation;
	/** 周边兴趣*/
	private TextView mRoundInter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mProgress = new ProgressDialog(mContext);
		mProgress.setMessage("正在定位您当前位置..");
		mProgress.show();
		
		setContentView(R.layout.activity_main);
		mCitySearch = (TextView) findViewById(R.id.city_search);
		mRoadNavigation = (TextView) findViewById(R.id.road_navigation);
		mRoundInter = (TextView) findViewById(R.id.around_inst);
		initListener();
//		Intent intent = getIntent();
		// 获取定位服务
		mLocationClient = ((NavigationApplication) getApplication()).mLocationClient;
//		if (intent.hasExtra("x") && intent.hasExtra("y")) {
//			// 当用intent参数时，设置中心点为指定点
//			Bundle b = intent.getExtras();
//			LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
//			mMapView = new MapView(this,
//					new BaiduMapOptions().mapStatus(new MapStatus.Builder()
//							.target(p).build()));
//		} else {
//			mMapView = new MapView(this, new BaiduMapOptions());
//		}
//		setContentView(mMapView);
//		mBaiduMap = mMapView.getMap();
		mHandler = new TaskHandler();
		// 注册广播
		regsiterKeyValidate();
		//初始化定位参数
		InitLocation();
		//获取通知管理器
		mNotificationManager =  (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				try {
					//延迟3秒发送消息，以保证有足够的时间获取位置信息
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				mLocationList = ((NavigationApplication) getApplication()).mLocationListInfo;
				
				mHandler.sendMessage(msg);
			}

		}).start();
	}
	/**
	 * 单击事件
	 */
	private void initListener(){
		
	}
	private class ClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			Intent intent = null;
           switch (v.getId()) {
           //城市查询
		case R.id.city_search:
			intent = new Intent();
			intent.setClass(mContext, CitySearchActivity.class);
			startActivity(intent);
			break;
			//路线导航
		case R.id.road_navigation:
			intent = new Intent();
			intent.setClass(mContext, RouteNavigationActivity.class);
			startActivity(intent);
			break;
			//周边兴趣
		case R.id.around_inst:
			intent = new Intent();
			intent.setClass(mContext, InterestActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}			
		}
		
	}
	/**
	 * 初始化通知栏
	 */
	private void initNotification(String locationInfo){
		android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
		 Drawable draw = mContext.getResources().getDrawable(R.drawable.ic_launcher);
		 builder.setLargeIcon(ImageTool.drawableToBitmap(draw)).setSmallIcon(R.drawable.ic_launcher);
         builder.setTicker("showNormal").setContentInfo("contentInfo");
         builder.setContentTitle("您当前所在位置是").setContentText(locationInfo);
         builder.setNumber(5);
		 builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
		 //创建Notification对象
		 Notification notification = builder.build();
         mNotificationManager.notify(Constants.NTFY_LOCATION, notification);
	}

	/**
	 * 异步任务消息，当获取到新的定位信息后就刷新界面
	 * 
	 * @author: liwei
	 * @Description: TODO
	 * @date: 2015年4月21日
	 */
	private class TaskHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String address = "";
				LocationInfo locationInfo = null;
				if (mLocationList != null && mLocationList.size() != 0) {
					locationInfo =mLocationList.get(mLocationList.size() - 1);
					address = locationInfo.address;
					//根据经纬度构造地图
					LatLng p = new LatLng(locationInfo.latitude, locationInfo.lontitude);
					mMapView = new MapView(mContext,new BaiduMapOptions().mapStatus(new MapStatus.Builder()
									.target(p).build()));
					
					//发送通知
					initNotification(address);
				} else {
					address = "无法获取地理位置信息";
					//如果没有定位成功就默认当前地点为北京
					mMapView = new MapView(mContext, new BaiduMapOptions());
				}
				Toast.makeText(mContext, address, Toast.LENGTH_SHORT).show();
				//定位后设置地图
				mProgress.dismiss();
//				setContentView(mMapView);
				mBaiduMap = mMapView.getMap();
				break;

			default:
				break;
			}
			
		}
	}

	/**
	 * 注册key值验证广播，主要是在开发时用于确定能否调用百度地图api
	 */
	private void regsiterKeyValidate() {
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mKeyReceiver = new KeyValidateReceiver();
		registerReceiver(mKeyReceiver, iFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		if(mMapView !=null){
			mMapView.onResume();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		if(mMapView !=null){
			mMapView.onResume();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		if(mMapView !=null){
			mMapView.onResume();
		}
	}

	/**
	 * 初始化定位
	 */
	private void InitLocation() {
		// 定位参数对象
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		// option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		// 定位时间间隔
		// int span=1000;
		mSpaneFrequence = 3000;
		option.setScanSpan(mSpaneFrequence);// 设置发起定位请求的间隔时间为1000ms
		// 是否返回地址
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		mLocationClient.setLocOption(option);
		mLocationClient.setLocOption(option);
		//开始定位
		mLocationClient.start();
	}
}
