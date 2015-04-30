/**
 * 
 */
package com.scho.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

import com.scho.Constants;
import com.scho.client.SchoolInfoClient;
import com.scho.entity.EventsInfo;
import com.scho.entity.MemoInfo;
import com.scho.ui.R;
import com.scho.widget.NavigationListView;
import com.scho.widget.NavigationListView.OnRefreshListener;

/**
 * @author:  liwei
 * @Description:  个人备忘录 
 * @date:  2015年4月20日
 */
public class PersonalMemoActivity extends Activity{
    /** 上下文*/
	private Context mContext;
	/** 数据列表*/
	private NavigationListView mListView;
	/** 数据源*/
	private ArrayList<MemoInfo> mDataSource = new ArrayList<MemoInfo>();
	/** 适配器*/
	private ListAdapter mDataAdapter;
	/** 异步任务*/
	private TaskHandler mHandler;
	/** 删除进度*/
	private ProgressDialog mProgressDele;
	 /** 长按和单击选中的备忘录*/
		private MemoInfo mSelMemoInfo;
    /** 当前数据item条数*/
		private int mOldItemCount;
		/** 本页面的条数*/
		private int mCurItemCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_memo);
		mOldItemCount = getIntent().getIntExtra("item_count",-1);
		mContext = this;
		mListView = (NavigationListView) findViewById(R.id.personal_memo);
		
		mProgressDele = new ProgressDialog(mContext);
		mProgressDele.setMessage("正在删除...");
		
		mHandler = new TaskHandler();
		getDataFromServer();
		mListView.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 这里开启子线程执行联网操作
				getDataFromServer();
			}
		});
		
		//为ListView注册长按事件
	registerForContextMenu(mListView);
	}
	 @Override  
	    public void onCreateContextMenu(ContextMenu menu, View v,  
	            ContextMenuInfo menuInfo) {  
	            menu.setHeaderTitle("备忘录删除");  
	            //添加菜单项  
	            menu.add(0,Menu.FIRST,0,"删除");  
	        super.onCreateContextMenu(menu, v, menuInfo);  
	    }  
	      
	    @Override  
	    public boolean onContextItemSelected(MenuItem item) {  
	        AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();  
//	        int position = (int) mEventsListView.getAdapter().getItemId(info.position);
	        int position = info.position-1;
	        if(position >=0 && position < mDataSource.size()){
	        	mSelMemoInfo = mDataSource.get(position);
	        }
	        switch (item.getItemId()) {
			case 1:
				//删除选中的
				mProgressDele.show();
			    new Thread(new Runnable(){

					@Override
					public void run() {
						Message msg = mHandler.obtainMessage();
						msg.what = 1;
						//不为空就删除
						if(mSelMemoInfo !=null){
							msg.obj =  SchoolInfoClient.deleteEvents(mSelMemoInfo.memoId, Constants.SCHO_PERSONLA_MEMO);
						}else{
							msg.obj = false;
						}
						mHandler.sendMessage(msg);
					}
			    	
			    }).start();
			 
				break;

			default:
				break;
			}
	        return super.onContextItemSelected(item);  
	    }  
	/**
	 * 
	 * @author: liwei
	 * @Description: 执行异步处理
	 * @date: 2015年4月16日
	 */
	private class TaskHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mDataAdapter = new ListAdapter(mContext, mDataSource);
				mListView.setAdapter(mDataAdapter);
				mCurItemCount = mDataSource.size();
				// 刷新结束
				mListView.onRefreshComplete();
				break;
			case 1:
				mProgressDele.dismiss();
				boolean isSuccess = (Boolean) msg.obj;
				if(isSuccess){
					getDataFromServer();
					Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(mContext, "删除失败，请检查网络状态", Toast.LENGTH_LONG).show();
				}
			default:
				break;
			}
		}
	}
	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				mDataSource = SchoolInfoClient.getMemoClient();
				Message msg = mHandler.obtainMessage();
				msg.what = 0;
				mHandler.sendMessage(msg);
			}

		}).start();
	}
	/**
	 * 数据列表适配器
	 * 
	 * @author: liwei
	 * @Description: 活动列表适配器
	 * @date: 2015年4月16日
	 */
	private class ListAdapter extends BaseAdapter {
		/** 数据源 */
		ArrayList<MemoInfo> objects;
		/** 加载器 */
		LayoutInflater inflater;

		public ListAdapter(Context context, ArrayList<MemoInfo> objects) {
			this.objects = objects;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			if (objects != null) {
				return objects.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (objects != null) {
				return objects.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.memo_list_item,
						null);
				viewHolder.createTime = (TextView) convertView
						.findViewById(R.id.memo_createtime);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.memo_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MemoInfo memoInfo = objects.get(position);
			// 开始赋值
			viewHolder.createTime.setText(memoInfo.createTime);
			viewHolder.content.setText(memoInfo.content);

			return convertView;
		}

		class ViewHolder {
			/** 创建时间 */
			TextView createTime;
			/** 备忘录内容 */
			TextView content;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==14 && resultCode ==15){
			//当用户新增一条备忘录后返回后从服务器加载一次
			getDataFromServer();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent intent = new Intent();
			intent.setClass(mContext, AddNewMemoActivity.class);
			startActivityForResult(intent, 14);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(1, 1, 0, "新建备忘录");
		return true;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent();
		intent.putExtra("cur_count", intent);
		setResult(19,intent);
		
	}
	
}
