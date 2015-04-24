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
 * @Description:  �����б�
 * @date:  2015��4��16��
 */
public class WeatherFragment extends Fragment{
	 private static WeatherFragment mInstance;
	 /** ActionBar */
		private ActionBar mActionBar;
		/** �б����������� */
		private List<WeatherInfo> mMesList;
		/** Handler���󣬻�ȡ������� */
		private WeatherHandler mHandler;
		/** �����Ķ��� */
		private Context mContext;
		/** ��ѯ����url��ַ */
		private final static String weatherUrl = "http://api.k780.com:88/?app=weather.future&weaid=101200101&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
		/** ������ѯ��� */
		private String weatherResult;
		/** ����չʾ�б� */
		private NavigationListView mListView;
		/** ListView���������� */
		private ListAdapter mAdapter;
		/** �������� */
		private TextView mCityNameTextVIew;
		/** ���һ��ˢ��ʱ�� */
		private TextView mCurTimeTextView;
		/** ����ͼƬ */
		private ImageView mWeatherImage;
		/** ͼƬ*/
		private DisplayImageOptions mDisplayImageOptions;
	    //��ȡ��ҳ��Ƭʵ��
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
					.showImageOnFail(R.drawable.owidget_scatttered_thundershowers_day)                        //����ͼƬ����ʧ�ܵ�չʾͼƬ
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
		            	//ˢ�µ�ʱ��ӷ��������»�ȡ����
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
		 * ��ȡ������Ϣ
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
	     * ��ȡ����ͼƬ
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
		 * @Description: �첽����
		 * @date:  2015��4��16��
		 */
		private class WeatherHandler extends Handler {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					
					String weatherInfo = (String) msg.obj;
					if (null != weatherInfo) {
						// ��ʼ��json�ַ���������ʵ����
						mMesList = WeatherInfo.jsonToWeatherInfo(weatherInfo,mContext);
						//��һ�μ��ػ��������������Ժ�ÿ��ˢ�¾Ͳ���������
						if(mAdapter == null){
							mAdapter = new ListAdapter(mContext, mMesList);
							mListView.setAdapter(mAdapter);
						}
						//���������ݸı�
						mAdapter.notifyDataSetChanged();
						// ���ó���
						if(mMesList !=null && mMesList.size()!=0){
							mCityNameTextVIew.setText(mMesList.get(0).cityName);
						}
						// ����ʱ��
						mCurTimeTextView.setText(getCurTime() + " CST");
						//ˢ�½���
						mListView.onRefreshComplete();  
					}
				case 1:
					// ����ͼƬ�ı�
					if(mAdapter !=null)
					mAdapter.notifyDataSetChanged();
				default:
					break;
				}
			}
		}

		/**
		 * ��ȡ��ǰʱ�䣬���һ��ˢ�µ�ʱ��
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
		 * @Description:  ���������� 
		 * @date:  2015��4��16��
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
					//ͼƬû�м���֮ǰ�������Ԥ����Ƭ
//					viewHolder.image.setImageBitmap(weather.weatherBitmap);
					ImageLoader.getInstance().displayImage(weather.weaImageUrl, viewHolder.image,mDisplayImageOptions);
				}

				return convertView;
			}

			class ViewHolder {
				/** ���� */
				TextView weekTextView;
				/** ����ͼƬ */
				ImageView image;
				/** ����¶� */
				TextView maxTemperTextView;
				/** ��С�¶� */
				TextView minTemperTextVIew;
			}
		}
}
