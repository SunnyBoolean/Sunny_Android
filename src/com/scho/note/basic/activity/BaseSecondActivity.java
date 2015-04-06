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
 * @author ��ΰ
 * @Description:��������Ļ��࣬��ActionBar���⣬�Զ���ͷ������෵�ذ�ť��
 * @date:2015��3��7�� ����9:52:53
 */

public class BaseSecondActivity extends Activity {
	/** ActionBar*/
	public ActionBar mActionBar;
	/** �����Ķ���*/
    public Context mContext;
    /** �����¼�����*/
    public static SingleListener mSingleListener;
    /** �����¼��������࣬��handlerOnClick�г�ʼ��*/
    public static View mViewForListener;
    /** ����Activity��������*/
    public static final int REQUEST_CODE = 101;
    /** ����Activity�Ļ�Ӧ��*/
    public static final int RESPONCE_CODE = 102;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mContext = this;
		// ��ȡActionBar
		mActionBar = getActionBar();
		initCompontent();
		// ��ActionBar����һ������
		initActionBar();
		//��ʼ���¼�ע��
		initListener();
	}

	/**
	 * 
	 * Description: ��ʼ�������setContentView����
	 * 
	 * @author: ��ΰ
	 * @date:2015��3��7�� ����10:08:15
	 */
	public void initCompontent() {

	}

	/**
	 * 
	 * Description: ��ȡActionBar
	 * 
	 * @author: ��ΰ
	 * @date:2015��3��7�� ����10:06:14
	 */
	public void initActionBar() {
	}

	/**
	 * 
	 * Description: ע���¼�������ʹ��ʱ������super�˷������Դ���OnClickListenerʵ��
	 * 
	 * @author: ��ΰ
	 * @date:2015��3��8�� ����8:48:15
	 */
	public void initListener() {
		if( null == mSingleListener){
			mSingleListener = new SingleListener();
		}
	}
	/**
	 * 
	 * @author ��ΰ
	 * @Description:T�����¼��� 
	 * @date:2015��3��8�� ����8:51:44
	 */
	 private class SingleListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			handlerOnClick(v);
		}
		 
	 }
	 /**
	  * 
	  * Description: �������¼��������ֱ����д�˷�������
	  * @author: ��ΰ
	  * @date:2015��3��8�� ����8:52:41
	  */
	 public void handlerOnClick(View v){
		 mViewForListener = v;
	 }
}
