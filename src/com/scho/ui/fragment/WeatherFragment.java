/**
 * 
 */
package com.scho.ui.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scho.client.WeatherClientHelper;
import com.scho.entity.WeatherInfo;
import com.scho.ui.R;
import com.scho.widget.NavigationListView;
import com.scho.widget.NavigationListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author:  liwei
 * @Description:  天气列表
 * @date:  2015年4月16日
 */
public class WeatherFragment extends Fragment{
	 private static WeatherFragment mInstance;
	 /** ActionBar */
		private ActionBar mActionBar;
		/** 列表适配器数据 */
		private List<WeatherInfo> mMesList;
		/** Handler对象，获取网络服务 */
		private WeatherHandler mHandler;
		/** 上下文对象 */
		private Context mContext;
		/** 查询天气url地址 */
		private final static String weatherUrl = "http://api.k780.com:88/?app=weather.future&weaid=101200101&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
		/** 天气查询结果 */
		private String weatherResult;
		/** 天气展示列表 */
		private NavigationListView mListView;
		/** ListView数据适配器 */
		private ListAdapter mAdapter;
		/** 城市名字 */
		private TextView mCityNameTextVIew;
		/** 最后一次刷新时间 */
		private TextView mCurTimeTextView;
		/** 天气图片 */
		private ImageView mWeatherImage;
		/** 图片*/
		private DisplayImageOptions mDisplayImageOptions;
	    //获取首页碎片实例
	    public static WeatherFragment getInstance(){
	    	if(mInstance == null){
	    		mInstance = new WeatherFragment();
	    	}
	    	return mInstance;
	    }
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
		}
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			mContext = activity;
			mDisplayImageOptions = new DisplayImageOptions.Builder().cacheOnDisc()
					.cacheInMemory().showStubImage(R.drawable.owidget_scatttered_thundershowers_day)
					.showImageOnFail(R.drawable.owidget_scatttered_thundershowers_day)                        //设置图片加载失败的展示图片
					.showImageForEmptyUri(R.drawable.owidget_scatttered_thundershowers_day).build();
			super.onAttach(activity);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.weather_list, null);
			mListView = (NavigationListView) view.findViewById(R.id.main_list);
			View head = inflater.inflate(R.layout.layout_head, null);
			mListView.addHeaderView(head);
			mListView.setonRefreshListener(new OnRefreshListener() {  
		            @Override  
		            public void onRefresh() {
		            	//刷新的时候从服务器重新获取天气
		                getWeatherInfo();
		            }  
		        });  
			mCityNameTextVIew = (TextView) view.findViewById(R.id.city_name);
			mCurTimeTextView = (TextView) view.findViewById(R.id.cur_time);
			mHandler = new WeatherHandler();
			getWeatherInfo();
			return view;
		}
		/**
		 * 获取天气信息
		 */
		private void getWeatherInfo() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					weatherResult = new WeatherClientHelper(mContext)
							.getWeatherFromServe(weatherUrl);
					Message msg = mHandler.obtainMessage();
					msg.obj = weatherResult;
					msg.what = 0;
					mHandler.sendMessage(msg);
				}

			}).start();
		}
		 /**
	     * 获取天气图片
	     */
//		private void getWeatherImage() {
//			for (int i = 0; i < mMesList.size(); i++) {
//				final int index = i;
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						Bitmap bitmap = WeatherInfo.getWeatherImage(mMesList
//								.get(index).weaImageUrl);
//							mMesList.get(index).weatherBitmap = bitmap;
//					}
//
//				}).start();
//			}
//				Message msgs = mHandler.obtainMessage();
//				msgs.what = 1;
//				msgs.obj = mMesList;
//				mHandler.sendMessage(msgs);
//		}
		/**
		 * 
		 * @author:  liwei
		 * @Description: 异步处理
		 * @date:  2015年4月16日
		 */
		private class WeatherHandler extends Handler {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					
					String weatherInfo = (String) msg.obj;
					if (null != weatherInfo) {
						// 开始将json字符串解析成实体类
						mMesList = WeatherInfo.jsonToWeatherInfo(weatherInfo,mContext);
						//第一次加载会设置适配器，以后每次刷新就不重新设置
						if(mAdapter == null){
							mAdapter = new ListAdapter(mContext, mMesList);
							mListView.setAdapter(mAdapter);
						}
						//适配器数据改变
						mAdapter.notifyDataSetChanged();
						// 设置城市
						if(mMesList !=null && mMesList.size()!=0){
							mCityNameTextVIew.setText(mMesList.get(0).cityName);
						}
						// 设置时间
						mCurTimeTextView.setText(getCurTime() + " CST");
						//刷新结束
						mListView.onRefreshComplete();  
					}
				case 1:
					// 天气图片改变
					if(mAdapter !=null)
					mAdapter.notifyDataSetChanged();
				default:
					break;
				}
			}
		}

		/**
		 * 获取当前时间，最后一次刷新的时间
		 * 
		 * @return
		 */
		public String getCurTime() {
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("HH:mm:ss");
			return format.format(date);

		}
		/**
		 * 
		 * @author:  liwei
		 * @Description:  数据适配器 
		 * @date:  2015年4月16日
		 */
		public class ListAdapter extends BaseAdapter {

			int resource;
			LayoutInflater layoutInfl;
			List<WeatherInfo> tempList;
			private ViewHolder viewHolder;

			public ListAdapter(Context context, List<WeatherInfo> list) {
				this.resource = resource;
				tempList = list;
				layoutInfl = LayoutInflater.from(context);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mMesList.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return mMesList.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub

				return 0;
			}

			@SuppressLint("NewApi")
			@Override
			public View getView(int position, View convertView, ViewGroup viewGroup) {
				if (convertView == null && null != layoutInfl) {
					viewHolder = new ViewHolder();
					convertView = layoutInfl.inflate(R.layout.layout_mainlist_item,
							null);
					viewHolder.weekTextView = (TextView) convertView
							.findViewById(R.id.week_name);
					viewHolder.maxTemperTextView = (TextView) convertView
							.findViewById(R.id.max_temper);
					viewHolder.minTemperTextVIew = (TextView) convertView
							.findViewById(R.id.min_temper);
					viewHolder.image = (ImageView) convertView
							.findViewById(R.id.weather_image_show);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				WeatherInfo weather = (WeatherInfo) getItem(position);
				if (null != weather && null != viewHolder) {
					viewHolder.weekTextView.setText(weather.weekName);
					viewHolder.maxTemperTextView.setText(weather.maxTemper);
					viewHolder.minTemperTextVIew.setText(weather.minTemper);
					//图片没有加载之前就用这个预备照片
//					viewHolder.image.setImageBitmap(weather.weatherBitmap);
					ImageLoader.getInstance().displayImage(weather.weaImageUrl, viewHolder.image,mDisplayImageOptions);
				}

				return convertView;
			}

			class ViewHolder {
				/** 星期 */
				TextView weekTextView;
				/** 天气图片 */
				ImageView image;
				/** 最大温度 */
				TextView maxTemperTextView;
				/** 最小温度 */
				TextView minTemperTextVIew;
			}
		}
}
