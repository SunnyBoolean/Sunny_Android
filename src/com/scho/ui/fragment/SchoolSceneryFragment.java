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
 * @Description:  У԰�羰
 * @date:  2015��4��17��
 */
public class SchoolSceneryFragment extends Fragment{
	    /** ������*/
		private Context mContext;
		/** ��Ƭʵ��*/
		private static SchoolSceneryFragment mInstance;
		/** ImageViewչʾ��ͼ*/
		private ImageView mShowImage;
		/** ����*/
		private Gallery mGallery;
		/** ͼƬ����Դ*/
		private ArrayList<Integer> mImageResourceIds;
		/** ImageAdapter*/
		private ImageAdapter mAdapter;
		/**
		 * ��ȡ��Ƭ����ʵ��
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
		 * ��ʼ������Դ���
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
		 * ��ʼ��������
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
		 * @author:  ����������
		 * @Description:  TODO 
		 * @date:  2015��4��20��
		 */
	    public class ImageAdapter extends BaseAdapter {

	        Context mContext;        //�����Ķ���
			
	        
	        //���췽��
	        public ImageAdapter(Context context) {
	            this.mContext = context;
	        }
	        
	        //��ȡͼƬ�ĸ���
	        public int getCount() {
	            return mImageResourceIds.size();
	        }

	        //��ȡͼƬ�ڿ��е�λ��
	        public Object getItem(int position) {
	            return mImageResourceIds.get(position);
	        }

	        //��ȡͼƬ�ڿ��е�λ��
	        public long getItemId(int position){
	            return position;
	        }

	        //��ȡ��������ָ��λ�õ���ͼ����
	        public View getView(int position, View convertView, ViewGroup parent) {
	            ImageView imageView = new ImageView(mContext);
	            imageView.setImageResource(mImageResourceIds.get(position));
	            imageView.setLayoutParams(new Gallery.LayoutParams(120, 120));
	            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	            return imageView;
	        }
	        
	    }
}
