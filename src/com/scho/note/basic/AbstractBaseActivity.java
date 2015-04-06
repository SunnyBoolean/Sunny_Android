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
 * @Description: ����Activity�Ļ��࣬��Ŀ��ÿһ������ģ��Ļ��඼�̳д��࣬�������ȡApplication���� �����������������
 * @date:2015��1��30��
 * @FileName:AbstractBaseActivity
 */
public  abstract class AbstractBaseActivity extends SherlockFragmentActivity{
    /** Application����*/
	public NoteApplication mApplication;
	/** Context����*/
	public Context mContext;
	/** ActionBar����*/
	public ActionBar mActionBar;
	/** �첽���������*/
	public ProgressDialog mProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (NoteApplication) getApplication();
		mContext = this;
		//����������
		mProgress = new ProgressDialog(mContext);
		initActionBar();
		initContentStart();
	}
	/**
	 * ActionBar���������
	 */
	public void initActionBar(){
		mActionBar = getSupportActionBar();
		//����actionbar�ĸ߶�
		mApplication.appData.put(ActionBarAttrKey.ACTIONABR_HEIGHT, getActionbarHeight());
	}
	/**
	 * 
	 * Description: �ؼ���ʼ�����,�˷������뱻ʵ��
	 * @author: liwei
	 * @date:2015��2��1�� ����4:10:18
	 */
	public void initContentStart(){
		initCompontent();
		initSourceData();
		initAdapter();
		initListener();
	}
	/**
	 * 
	 * Description: ��ȡActionbar�ĸ߶�
	 * @author: liwei
	 * @date:2015��2��2�� ����10:33:58
	 * @return  �߶�Ϊ������
	 */
	public float getActionbarHeight(){
		TypedArray actionbarSizeTypedArray = mContext.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize  });  
		  float h = actionbarSizeTypedArray.getDimension(0, 0);
		  return h;
	}
	/**
	 * 
	 * Description: ��ʼ���ؼ������￪ʼ��ִ��setContentView()�������Լ�findViewById()����
	 * @author: liwei
	 * @date:2015��2��1�� ����4:10:08
	 */
	public abstract void initCompontent();
	/**
	 * 
	 * Description: ��ʼ������Դ����������������Դ�������ݿ��ȡ�����ݵ�
	 * @author: liwei
	 * @date:2015��2��1�� ����4:10:27
	 */
	public abstract void initSourceData();
	/**
	 * 
	 * Description: ��ʼ��������������еĻ�
	 * @author: liwei
	 * @date:2015��2��1�� ����4:10:39
	 */
	public abstract void initAdapter();
	/**
	 * 
	 * Description: ��ʼ��������
	 * @author: liwei
	 * @date:2015��2��1�� ����4:10:48
	 */
	public abstract void initListener();
	
}
