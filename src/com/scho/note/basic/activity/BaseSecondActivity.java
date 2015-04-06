/**
 * 
 */
package com.scho.note.basic.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author 李伟
 * @Description:二级界面的基类，无ActionBar主题，自定义头部，左侧返回按钮，
 * @date:2015年3月7日 下午9:52:53
 */

public class BaseSecondActivity extends Activity {
	/** ActionBar*/
	public ActionBar mActionBar;
	/** 上下文对象*/
    public Context mContext;
    /** 单击事件对象*/
    public static SingleListener mSingleListener;
    /** 用于事件监听的类，在handlerOnClick中初始化*/
    public static View mViewForListener;
    /** 启动Activity的请求码*/
    public static final int REQUEST_CODE = 101;
    /** 返回Activity的回应吗*/
    public static final int RESPONCE_CODE = 102;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mContext = this;
		// 获取ActionBar
		mActionBar = getActionBar();
		initCompontent();
		// 对ActionBar进行一定操作
		initActionBar();
		//初始化事件注册
		initListener();
	}

	/**
	 * 
	 * Description: 初始化组件，setContentView（）
	 * 
	 * @author: 李伟
	 * @date:2015年3月7日 下午10:08:15
	 */
	public void initCompontent() {

	}

	/**
	 * 
	 * Description: 获取ActionBar
	 * 
	 * @author: 李伟
	 * @date:2015年3月7日 下午10:06:14
	 */
	public void initActionBar() {
	}

	/**
	 * 
	 * Description: 注册事件监听，使用时必须先super此方法，以创建OnClickListener实例
	 * 
	 * @author: 李伟
	 * @date:2015年3月8日 下午8:48:15
	 */
	public void initListener() {
		if( null == mSingleListener){
			mSingleListener = new SingleListener();
		}
	}
	/**
	 * 
	 * @author 李伟
	 * @Description:T单击事件类 
	 * @date:2015年3月8日 下午8:51:44
	 */
	 private class SingleListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			handlerOnClick(v);
		}
		 
	 }
	 /**
	  * 
	  * Description: 处理单击事件，子类均直接重写此方法即可
	  * @author: 李伟
	  * @date:2015年3月8日 下午8:52:41
	  */
	 public void handlerOnClick(View v){
		 mViewForListener = v;
	 }
}
