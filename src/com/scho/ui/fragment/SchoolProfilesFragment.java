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
 * @Description:  У԰���Ƭ���� 
 * @date:  2015��4��17��
 */
public class SchoolProfilesFragment extends Fragment{
    /** ������*/
	private Context mContext;
	/** ��Ƭʵ��*/
	private static SchoolProfilesFragment mInstance;
	/** WebView�鿴ѧУ���*/
	private WebView mSchoolProfileWebView;
	/**
	 * ��ȡ��Ƭ����ʵ��
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
		mSchoolProfileWebView.setBackgroundColor(Color.TRANSPARENT);  //  WebView ����͸��Ч�� 
		mSchoolProfileWebView.loadUrl("file:///android_asset/scho/school_profile.html");
		return view;
	}
	/**
	 * ��ʼ��web����
	 */
	public void initWebSetings(){
		WebSettings wsetting = mSchoolProfileWebView.getSettings();
		wsetting.getJavaScriptCanOpenWindowsAutomatically();
		
	}
   
}
