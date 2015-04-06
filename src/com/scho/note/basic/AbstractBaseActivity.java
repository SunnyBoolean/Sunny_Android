/**
 * 
 */
package com.scho.note.basic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.scho.note.Constants.ActionBarAttrKey;
import com.scho.note.system.NoteApplication;


/**
 * 
 * @author liwei
 * @ClassName: AbstractBaseActivity.java
 * @Description: 所有Activity的基类，项目中每一个功能模块的基类都继承此类，在这里获取Application对象 ，定义程序运行流程
 * @date:2015年1月30日
 * @FileName:AbstractBaseActivity
 */
public  abstract class AbstractBaseActivity extends SherlockFragmentActivity{
    /** Application对象*/
	public NoteApplication mApplication;
	/** Context对象*/
	public Context mContext;
	/** ActionBar对象*/
	public ActionBar mActionBar;
	/** 异步处理进度条*/
	public ProgressDialog mProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (NoteApplication) getApplication();
		mContext = this;
		//创建进度条
		mProgress = new ProgressDialog(mContext);
		initActionBar();
		initContentStart();
	}
	/**
	 * ActionBar的相关设置
	 */
	public void initActionBar(){
		mActionBar = getSupportActionBar();
		//保存actionbar的高度
		mApplication.appData.put(ActionBarAttrKey.ACTIONABR_HEIGHT, getActionbarHeight());
	}
	/**
	 * 
	 * Description: 控件初始化入口,此方法必须被实现
	 * @author: liwei
	 * @date:2015年2月1日 下午4:10:18
	 */
	public void initContentStart(){
		initCompontent();
		initSourceData();
		initAdapter();
		initListener();
	}
	/**
	 * 
	 * Description: 获取Actionbar的高度
	 * @author: liwei
	 * @date:2015年2月2日 上午10:33:58
	 * @return  高度为浮点型
	 */
	public float getActionbarHeight(){
		TypedArray actionbarSizeTypedArray = mContext.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize  });  
		  float h = actionbarSizeTypedArray.getDimension(0, 0);
		  return h;
	}
	/**
	 * 
	 * Description: 初始化控件从这里开始，执行setContentView()方法，以及findViewById()方法
	 * @author: liwei
	 * @date:2015年2月1日 下午4:10:08
	 */
	public abstract void initCompontent();
	/**
	 * 
	 * Description: 初始化数据源，如适配器的数据源，从数据库获取的数据等
	 * @author: liwei
	 * @date:2015年2月1日 下午4:10:27
	 */
	public abstract void initSourceData();
	/**
	 * 
	 * Description: 初始化适配器，如果有的话
	 * @author: liwei
	 * @date:2015年2月1日 下午4:10:39
	 */
	public abstract void initAdapter();
	/**
	 * 
	 * Description: 初始化监听器
	 * @author: liwei
	 * @date:2015年2月1日 下午4:10:48
	 */
	public abstract void initListener();
	
}
