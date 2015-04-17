/**
 * 
 */
package com.scho.ui.fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
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
 * @Description:  ��ҳ��Ƭ����
 * @date:  2015��4��16��
 */
public class HomeFragment extends Fragment{
	/** ��Ƭʵ��*/
    private static HomeFragment mInstance;
    /** �����Ļ���*/
    private Context mContext;
    /** ͼƬ*/
	private DisplayImageOptions mDisplayImageOptions;
    //��ȡ��ҳ��Ƭʵ��
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
		mDisplayImageOptions = new DisplayImageOptions.Builder().cacheOnDisc()
				.cacheInMemory().showStubImage(R.drawable.placeholder_image)
				.showImageOnFail(R.drawable.placeholder_image)                        //����ͼƬ����ʧ�ܵ�չʾͼƬ
				.displayer(new RoundedBitmapDisplayer(35))   //����Բ��ͼƬ
				.showImageForEmptyUri(R.drawable.placeholder_image).build();
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