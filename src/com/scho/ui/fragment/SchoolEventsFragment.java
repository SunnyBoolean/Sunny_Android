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
 * @author:  hello
 * @Description:  TODO 
 * @date:  2015��4��16��
 */
public class SchoolEventsFragment extends Fragment{
	 private static SchoolEventsFragment mInstance;
	    //��ȡ��ҳ��Ƭʵ��
	    public static SchoolEventsFragment getInstance(){
	    	if(mInstance == null){
	    		mInstance = new SchoolEventsFragment();
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
			View view = inflater.inflate(R.layout.layout_schoolevents, null);
			return view;
		}
}
