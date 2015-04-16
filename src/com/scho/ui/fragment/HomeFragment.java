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
 * @author:  hello
 * @Description:  首页碎片界面
 * @date:  2015年4月16日
 */
public class HomeFragment extends Fragment{
	/** 碎片实例*/
    private static HomeFragment mInstance;
    /** 上下文环境*/
    private Context mContext;
    //获取首页碎片实例
    public static HomeFragment getInstance(){
    	if(mInstance == null){
    		mInstance = new HomeFragment();
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
		super.onAttach(activity);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_home, null);
		return view;
	}
    
}
