/**
 * 
 */
package com.scho.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.scho.ui.R;

/**
 * @author:  liwei
 * @Description:  校园风景
 * @date:  2015年4月17日
 */
public class SchoolSceneryFragment extends Fragment{
	    /** 上下文*/
		private Context mContext;
		/** 碎片实例*/
		private static SchoolSceneryFragment mInstance;
		/** ImageView展示大图*/
		private ImageView mShowImage;
		/** 画廊*/
		private Gallery mGallery;
		/** 图片数据源*/
		private ArrayList<Integer> mImageResourceIds;
		/** ImageAdapter*/
		private ImageAdapter mAdapter;
		/**
		 * 获取碎片界面实例
		 * @return
		 */
		public static SchoolSceneryFragment getInstance(){
			if(mInstance == null){
				mInstance = new SchoolSceneryFragment();
			}
			return mInstance;
		}
		
		
		@Override
		public void onAttach(Activity activity) {
			mContext = activity;
			super.onAttach(activity);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.school_scenery, null);
			mShowImage = (ImageView) view.findViewById(R.id.imageView);
			mGallery = (Gallery) view.findViewById(R.id.gallery);
			initDataSource();
			mAdapter = new ImageAdapter(mContext);
			mGallery.setAdapter(mAdapter);
			initListener();
			return view;
		}
		/**
		 * 初始化数据源搞基
		 */
		public void initDataSource(){
			if(mImageResourceIds == null){
				mImageResourceIds = new ArrayList<Integer>();
			}
			mImageResourceIds.add(R.drawable.guihua);
			mImageResourceIds.add(R.drawable.huxin);
			mImageResourceIds.add(R.drawable.linyinxiaodao);
			mImageResourceIds.add(R.drawable.tiane);
			mImageResourceIds.add(R.drawable.tianjinc);
			mImageResourceIds.add(R.drawable.tushuguan);
			mImageResourceIds.add(R.drawable.xiaoyuan);
			mImageResourceIds.add(R.drawable.xueghai);
			mImageResourceIds.add(R.drawable.yijiao);
		}
		/**
		 * 初始化监听器
		 */
		private void initListener(){
			mGallery.setOnItemClickListener(new OnItemClickListener() {
				   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				             mShowImage.setBackgroundResource(mImageResourceIds.get(arg2));
				       }
				    });
		}
		/**
		 * 
		 * @author:  画廊适配器
		 * @Description:  TODO 
		 * @date:  2015年4月20日
		 */
	    public class ImageAdapter extends BaseAdapter {

	        Context mContext;        //上下文对象
			
	        
	        //构造方法
	        public ImageAdapter(Context context) {
	            this.mContext = context;
	        }
	        
	        //获取图片的个数
	        public int getCount() {
	            return mImageResourceIds.size();
	        }

	        //获取图片在库中的位置
	        public Object getItem(int position) {
	            return mImageResourceIds.get(position);
	        }

	        //获取图片在库中的位置
	        public long getItemId(int position){
	            return position;
	        }

	        //获取适配器中指定位置的视图对象
	        public View getView(int position, View convertView, ViewGroup parent) {
	            ImageView imageView = new ImageView(mContext);
	            imageView.setImageResource(mImageResourceIds.get(position));
	            imageView.setLayoutParams(new Gallery.LayoutParams(120, 120));
	            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	            return imageView;
	        }
	        
	    }
}
