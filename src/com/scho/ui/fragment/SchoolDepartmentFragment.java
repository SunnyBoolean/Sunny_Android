/**
 * 
 */
package com.scho.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.scho.ui.R;

/**
 * @author:  hello
 * @Description:  TODO 
 * @date:  2015��4��17��
 */
public class SchoolDepartmentFragment extends Fragment{
	 /** ������*/
	private Context mContext;
	/** ��Ƭʵ��*/
	private static SchoolDepartmentFragment mInstance;
	/** webview*/
	private WebView mSchoDepartmentView;
	/**
	 * ��ȡ��Ƭ����ʵ��
	 * @return
	 */
	public static SchoolDepartmentFragment getInstance(){
		if(mInstance == null){
			mInstance = new SchoolDepartmentFragment();
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
		View view = inflater.inflate(R.layout.school_department, null);
		mSchoDepartmentView = (WebView) view.findViewById(R.id.scho_department);
		mSchoDepartmentView.loadUrl("http://www.hustwb.edu.cn/#");
		return view;
	}
}
