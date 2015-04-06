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
 * @date:2015��1��30��
 */

public class BaseApisUIFragment extends AbstractBaseFragment{
	/**
	 * ��ǰFragment���ڵ������Ļ���
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
		//��ʼ���ؼ�View
		initCompontent();
		//����Դ
		initSourceData();
		//������
		initAdapter();
		//������
		initListener();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
    /**
     * 
     * Description: ��ʼ��View��setCOntentView()������findViewById()����
     * @author: liwei
     * @date:2015��2��1�� ����4:09:10
     */
	public void initCompontent() {
	
	}

    /**
     * 
     * Description: ��ʼ������Դ��������ݿ��ȡ�����ݣ�
     * @author: liwei
     * @date:2015��2��1�� ����4:11:14
     */
	public void initSourceData() {
		
	}
    /**
     * 
     * Description: ��ʼ���������������������Լ���View������������
     * @author: liwei
     * @date:2015��2��1�� ����4:11:41
     */
	public void initAdapter() {
		
	}

    /**
     * 
     * Description: ��View�ؼ����¼�����
     * @author: liwei
     * @date:2015��2��1�� ����4:12:17
     */
	public void initListener() {
		// TODO Auto-generated method stub
	}


    
}
