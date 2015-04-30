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
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
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
	/** 客户端定位*/
	LocationClient mLocClient;
	/** 定位监听*/
	public MyLocationListenner myListener = new MyLocationListenner();
	/** 定位时间间隔，默认每个1s定位一次 */
	private int mSpaneFrequence = 1000;
	/** 定位信息 */
	private ArrayList<LocationInfo> mLocationList;
	/** Handler对象 */
	private TaskHandler mHandler;
	/** NotificationManager 管理通知栏，当用户进入某景点时就会出现通知*/
	private NotificationManager mNotificationManager;
	/** RadoButton事件监听*/
	private OnCheckedChangeListener radioButtonListener;
	private Button requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位
	/** 位置模式*/
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	
	/** 城市查询*/
	private TextView mCitySearch;
	/** 路线导航*/
	private TextView mRoadNavigation;
	/** 周边兴趣*/
	private TextView mRoundInter;
	/** 当前位置信息*/
	private LocationInfo mCurLocationInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		mCitySearch = (TextView) findViewById(R.id.city_search);
		mRoadNavigation = (TextView) findViewById(R.id.road_navigation);
		mRoundInter = (TextView) findViewById(R.id.around_inst);
		mMapView = (MapView) findViewById(R.id.home_mapview);
		requestLocButton = (Button) findViewById(R.id.button1);
		mCurrentMode = LocationMode.NORMAL;
		requestLocButton.setText("普通");
		//普通标准切换
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					requestLocButton.setText("跟随");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					requestLocButton.setText("普通");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					requestLocButton.setText("罗盘");
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);
		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioButtonListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.defaulticon) {
					// 传入null则，恢复默认图标
					mCurrentMarker = null;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, null));
				}
				if (checkedId == R.id.customicon) {
					// 修改为自定义marker
					mCurrentMarker = BitmapDescriptorFactory
							.fromResource(R.drawable.icon_geo);
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
							 		mCurrentMode, true, mCurrentMarker));
				}
			}
		};
		group.setOnCheckedChangeListener(radioButtonListener);
		//单击
		initListener();
		// 获取定位服务
		mLocationClient = ((NavigationApplication) getApplication()).mLocationClient;
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
				while(true){
				try {
					//每隔1分钟获取一次定位，以保证有足够的时间获取位置信息
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				mLocationList = ((NavigationApplication) getApplication()).mLocationListInfo;
				mHandler.sendMessage(msg);
				}
			}

		}).start();
		
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	/**
	 * 单击事件
	 */
	private void initListener(){
		ClickListener listener = new ClickListener();
		mCitySearch.setOnClickListener(listener);
		mRoadNavigation.setOnClickListener(listener);
		mRoundInter.setOnClickListener(listener);
		
	}
	private class ClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			Intent intent = null;
           switch (v.getId()) {
           //城市查询
		case R.id.city_search:
			intent = new Intent();
			intent.putExtra("curLocation", mCurLocationInfo);
			intent.setClass(mContext, CitySearchActivity.class);
			startActivity(intent);
			break;
			//路线导航
		case R.id.road_navigation:
			intent = new Intent();
			intent.setClass(mContext, RouteNavigationActivity.class);
			intent.putExtra("curLocation", mCurLocationInfo);
			startActivity(intent);
			break;
			//周边兴趣
		case R.id.around_inst:
			intent = new Intent();
			intent.setClass(mContext, InterestActivity.class);
			intent.putExtra("curLocation", mCurLocationInfo);
//			startActivity(intent);
			Toast.makeText(mContext, "此功能暂未开放", Toast.LENGTH_LONG).show();
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
					
				} else {
					address = "无法获取地理位置信息，检查网络或重启应用";
				}
				initNotification(address);
//				Toast.makeText(mContext, address, Toast.LENGTH_SHORT).show();
				break;
				
			default:
				break;
			}
			
		}
	}
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			mCurLocationInfo = new LocationInfo();
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			//经度
			mCurLocationInfo.lontitude = location.getLongitude();
			//纬度
			mCurLocationInfo.latitude = location.getLatitude();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				//重新设置当前定位的位置
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
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
//		option.setLocationMode(tempMode);// 设置定位模式
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
