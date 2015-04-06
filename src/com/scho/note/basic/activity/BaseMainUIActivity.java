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
 * @Description: Main界面的基类
 * @date:2015年1月30日
 */
public class BaseMainUIActivity extends AbstractBaseActivity{
	/** Handle 处理事件对象*/
	public BaseHandler mHandler  = new BaseHandler();
	
	
	
    /**
     * 对ActionBar进行管理设置以及初始化一个context对象
     */
	@Override
    public void initActionBar(){
		super.initActionBar();
		mActionBar.hide();
    	mContext = this;
    }
    @Override
    public void initCompontent(){
    	//创建一个Handler实例
    	
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
	 * Handler 处理
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
	 * Handle处理的具体方法,每一个继承此基类的类要使用Handle对象就必须重写此方法实现具体操作
	 * @param msg
	 */
	public void handleDetailMsg(Message msg){
		
	}
	/**
	 * 适配器
	 * @author hello
	 *
	 */
	public class UIAdapter extends BaseAdapter{
		//上下文对象
        Context context;
        //获取视图对象
        LayoutInflater inFlater;
        //资源id
        int resourceId;
        //适配的数据
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
