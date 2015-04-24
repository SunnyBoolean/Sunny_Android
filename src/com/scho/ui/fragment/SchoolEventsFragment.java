/**
 * 
 */
package com.scho.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract.EventsEntity;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetDataCallback;
import com.scho.Constants;
import com.scho.client.SchoolInfoClient;
import com.scho.entity.EventsInfo;
import com.scho.ui.R;
import com.scho.ui.activity.AddNewEventsActivity;
import com.scho.ui.activity.EventsDetailsActivity;
import com.scho.widget.NavigationListView;
import com.scho.widget.NavigationListView.OnRefreshListener;

/**
 * @author: liwei
 * @Description: 校园活动列表
 * @date: 2015年4月16日
 */
public class SchoolEventsFragment extends Fragment {
	/** 上下文 */
	private Context mContext;
	/** 碎片实例 */
	private static SchoolEventsFragment mInstance;
	/** ListView数据象 */
	private NavigationListView mEventsListView;
	/** 数据适配器 */
	private ListAdapter mDataAdapter;
	/** 数据源 */
	private ArrayList<EventsInfo> mEventsList;
	/** 异步处理类 */
	private TaskHandler mHandler;
    /** 长按和单击选中的互动*/
	private EventsInfo mSelEventInfo;
	/** 删除进度*/
	private ProgressDialog mProgressDele;
	// 获取首页碎片实例
	public static SchoolEventsFragment getInstance() {
		if (mInstance == null) {
			mInstance = new SchoolEventsFragment();
		}
		return mInstance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		//设置为true，否则此碎片的men不会被调用
		setHasOptionsMenu(true);
		mProgressDele = new ProgressDialog(mContext);
		mProgressDele.setMessage("正在删除..");
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.scho_event_list, null);
		mEventsListView = (NavigationListView) view
				.findViewById(R.id.scho_events);
		mHandler = new TaskHandler();
		getEventsData();
		mEventsListView.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 这里开启子线程执行联网操作
				getEventsData();
			}
		});
		//为ListView注册长按事件
		registerForContextMenu(mEventsListView);
		initListener();
		return view;
	}
	 @Override  
	    public void onCreateContextMenu(ContextMenu menu, View v,  
	            ContextMenuInfo menuInfo) {  
	            menu.setHeaderTitle("校园活动删除");  
	            //添加菜单项  
	            menu.add(0,Menu.FIRST,0,"删除");  
	        super.onCreateContextMenu(menu, v, menuInfo);  
	    }  
	      
	    @Override  
	    public boolean onContextItemSelected(MenuItem item) {  
	        AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();  
//	        int position = (int) mEventsListView.getAdapter().getItemId(info.position);
	        int position = info.position-1;
	        if(position >=0 && position < mEventsList.size()){
	        	mSelEventInfo = mEventsList.get(position);
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
						if(mSelEventInfo !=null){
							msg.obj =  SchoolInfoClient.deleteEvents(mSelEventInfo.eventsId, Constants.SCHO_EVENTS);
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
	 * 从服务器获取数据
	 */
	public void getEventsData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mEventsList = SchoolInfoClient.getEventsClient();
				Message msg = mHandler.obtainMessage();
				msg.what = 0;
				mHandler.sendMessage(msg);
			}

		}).start();
	}

	/**
	 * 视图监听器
	 */
	private void initListener() {
		
		//ListView单击监听
		mEventsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(mContext, EventsDetailsActivity.class);
				EventsInfo info = mEventsList.get(position-1);
				intent.putExtra("event", info);
				startActivity(intent);
			}
		});
		
		//ListView长按监听
//		mEventsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				mSelEventInfo = mEventsList.get(position-1);
//				return true;
//			}
//		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==12 && resultCode ==13){
			//当用户新增一条互动后返回后从服务器加载一次
			getEventsData();
		}
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
		ArrayList<EventsInfo> objects;
		/** 加载器 */
		LayoutInflater inflater;

		public ListAdapter(Context context, ArrayList<EventsInfo> objects) {
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
				convertView = inflater.inflate(R.layout.scho_event_list_item,
						null);
				viewHolder.atvContent = (TextView) convertView
						.findViewById(R.id.act_content);
				viewHolder.atvPerson = (TextView) convertView
						.findViewById(R.id.act_peraon);
				viewHolder.atvStartTime = (TextView) convertView
						.findViewById(R.id.starttime);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			EventsInfo event = objects.get(position);
			// 开始赋值
			viewHolder.atvContent.setText(event.eventContent);
			viewHolder.atvPerson.setText(event.personName);
			viewHolder.atvStartTime.setText(event.startTime);

			return convertView;
		}

		class ViewHolder {
			/** 活动发起人 */
			TextView atvPerson;
			/** 活动开始时间 */
			TextView atvStartTime;
			/** 活动内容 */
			TextView atvContent;
		}

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
				mDataAdapter = new ListAdapter(mContext, mEventsList);
				mEventsListView.setAdapter(mDataAdapter);
				// 刷新结束
				mEventsListView.onRefreshComplete();
				break;
			case 1:
				//删除成功后从服务器获取一次刘表
				mProgressDele.dismiss();
				boolean isSuccess = (Boolean) msg.obj;
				//如果删除成功就刷新列表
				if(isSuccess){
					getEventsData();
					Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(mContext, "删除失败，请检查网络状态", Toast.LENGTH_LONG).show();
				}
			default:
				break;
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.scho_events, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add:
			Intent intent = new Intent();
			intent.setClass(mContext, AddNewEventsActivity.class);
			startActivityForResult(intent, 12);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
