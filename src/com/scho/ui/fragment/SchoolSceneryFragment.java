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
 * @Description:  У԰�羰
 * @date:  2015��4��17��
 */
public class SchoolSceneryFragment extends Fragment{
	 /** ������*/
		private Context mContext;
		/** ��Ƭʵ��*/
		private static SchoolSceneryFragment mInstance;
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
			return view;
		}
}
