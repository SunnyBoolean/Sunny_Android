/**
 * 
 */
package com.scho.ui.fragment;


import com.scho.ui.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author:  liwei
 * @Description:  个人中心碎片 
 * @date:  2015年4月16日
 */
public class PersonalCenterFragment extends Fragment{
	 private static PersonalCenterFragment mInstance;
	    //获取首页碎片实例
	    public static PersonalCenterFragment getInstance(){
	    	if(mInstance == null){
	    		mInstance = new PersonalCenterFragment();
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
			super.onAttach(activity);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.layout_personalcenter, null);
			return view;
		}
}
