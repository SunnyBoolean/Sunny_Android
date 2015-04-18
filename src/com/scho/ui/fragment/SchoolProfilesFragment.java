/**
 * 
 */
package com.scho.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.scho.ui.R;

/**
 * @author:  liwei
 * @Description:  校园活动碎片界面 
 * @date:  2015年4月17日
 */
public class SchoolProfilesFragment extends Fragment{
    /** 上下文*/
	private Context mContext;
	/** 碎片实例*/
	private static SchoolProfilesFragment mInstance;
	/** WebView查看学校简介*/
	private WebView mSchoolProfileWebView;
	/**
	 * 获取碎片界面实例
	 * @return
	 */
	public static SchoolProfilesFragment getInstance(){
		if(mInstance == null){
			mInstance = new SchoolProfilesFragment();
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
		View view = inflater.inflate(R.layout.school_profile, null);
		mSchoolProfileWebView = (WebView) view.findViewById(R.id.school_profile_wv);
		initWebSetings();
		mSchoolProfileWebView.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果 
		mSchoolProfileWebView.loadUrl("file:///android_asset/scho/school_profile.html");
		return view;
	}
	/**
	 * 初始化web参数
	 */
	public void initWebSetings(){
		WebSettings wsetting = mSchoolProfileWebView.getSettings();
		wsetting.getJavaScriptCanOpenWindowsAutomatically();
		
	}
   
}
