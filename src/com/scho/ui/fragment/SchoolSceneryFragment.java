/**
 * 
 */
package com.scho.ui.fragment;

import com.scho.ui.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
			return view;
		}
}
