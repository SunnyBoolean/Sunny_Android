package com.scho.note.basic.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.scho.note.basic.AbstractBaseActivity;
/**
 * 
 * @author liwei
 * @Description: Main����Ļ���
 * @date:2015��1��30��
 */
public class BaseMainUIActivity extends AbstractBaseActivity{
	/** Handle �����¼�����*/
	public BaseHandler mHandler  = new BaseHandler();
	
	
	
    /**
     * ��ActionBar���й��������Լ���ʼ��һ��context����
     */
	@Override
    public void initActionBar(){
		super.initActionBar();
		mActionBar.hide();
    	mContext = this;
    }
    @Override
    public void initCompontent(){
    	//����һ��Handlerʵ��
    	
    }
    @Override
    public void initAdapter(){
    	
    }
    @Override
    public void initSourceData(){
    	
    }
    @Override
    public void initListener(){
    	
    }

	/**
	 * Handler ����
	 * @author hello
	 *
	 */
	public class BaseHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			handleDetailMsg(msg);
		}
	}
	/**
	 * Handle����ľ��巽��,ÿһ���̳д˻������Ҫʹ��Handle����ͱ�����д�˷���ʵ�־������
	 * @param msg
	 */
	public void handleDetailMsg(Message msg){
		
	}
	/**
	 * ������
	 * @author hello
	 *
	 */
	public class UIAdapter extends BaseAdapter{
		//�����Ķ���
        Context context;
        //��ȡ��ͼ����
        LayoutInflater inFlater;
        //��Դid
        int resourceId;
        //���������
        List<Object> adapterData;
        public UIAdapter(){
        	inFlater = LayoutInflater.from(context);
        }
        public UIAdapter(Context context,int resourceId,List<Object> adapterData){
        	this.context = context;
        	this.resourceId = resourceId;
        	this.adapterData = adapterData;
        	inFlater = LayoutInflater.from(context);
        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(adapterData != null){
				return adapterData.size();
			}else{
				return 0;
			}
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			if(adapterData !=null){
				return adapterData.get(arg0);
			}else{
				return null;
			}
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			return null;
		}
		
	}

    
}
