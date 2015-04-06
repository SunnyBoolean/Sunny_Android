/**
 * 
 */
package com.scho.note.basic.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;
import com.scho.note.basic.AbstractBaseFragment;

/**
 * @author hello
 * @Description:TODO 
 * @date:2015年1月30日
 */

public class BaseApisUIFragment extends AbstractBaseFragment{
	/**
	 * 当前Fragment所在的上下文环境
	 */
    public Context mContext;
	@Override
	public void onAttach(Activity activity) {
		if(null != activity){
			mContext = activity;
		}
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//初始化控件View
		initCompontent();
		//数据源
		initSourceData();
		//适配器
		initAdapter();
		//监听器
		initListener();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
    /**
     * 
     * Description: 初始化View，setCOntentView()方法和findViewById()方法
     * @author: liwei
     * @date:2015年2月1日 下午4:09:10
     */
	public void initCompontent() {
	
	}

    /**
     * 
     * Description: 初始化数据源，如从数据库获取的数据，
     * @author: liwei
     * @date:2015年2月1日 下午4:11:14
     */
	public void initSourceData() {
		
	}
    /**
     * 
     * Description: 初始化适配器，创建适配器以及各View的适配器设置
     * @author: liwei
     * @date:2015年2月1日 下午4:11:41
     */
	public void initAdapter() {
		
	}

    /**
     * 
     * Description: 各View控件的事件监听
     * @author: liwei
     * @date:2015年2月1日 下午4:12:17
     */
	public void initListener() {
		// TODO Auto-generated method stub
	}


    
}
