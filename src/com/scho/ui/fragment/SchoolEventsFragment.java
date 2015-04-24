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
 * @Description: У԰��б�
 * @date: 2015��4��16��
 */
public class SchoolEventsFragment extends Fragment {
	/** ������ */
	private Context mContext;
	/** ��Ƭʵ�� */
	private static SchoolEventsFragment mInstance;
	/** ListView������ */
	private NavigationListView mEventsListView;
	/** ���������� */
	private ListAdapter mDataAdapter;
	/** ����Դ */
	private ArrayList<EventsInfo> mEventsList;
	/** �첽������ */
	private TaskHandler mHandler;
    /** �����͵���ѡ�еĻ���*/
	private EventsInfo mSelEventInfo;
	/** ɾ������*/
	private ProgressDialog mProgressDele;
	// ��ȡ��ҳ��Ƭʵ��
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
		//����Ϊtrue���������Ƭ��men���ᱻ����
		setHasOptionsMenu(true);
		mProgressDele = new ProgressDialog(mContext);
		mProgressDele.setMessage("����ɾ��..");
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
				// ���￪�����߳�ִ����������
				getEventsData();
			}
		});
		//ΪListViewע�᳤���¼�
		registerForContextMenu(mEventsListView);
		initListener();
		return view;
	}
	 @Override  
	    public void onCreateContextMenu(ContextMenu menu, View v,  
	            ContextMenuInfo menuInfo) {  
	            menu.setHeaderTitle("У԰�ɾ��");  
	            //��Ӳ˵���  
	            menu.add(0,Menu.FIRST,0,"ɾ��");  
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
				//ɾ��ѡ�е�
				mProgressDele.show();
			    new Thread(new Runnable(){

					@Override
					public void run() {
						Message msg = mHandler.obtainMessage();
						msg.what = 1;
						//��Ϊ�վ�ɾ��
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
	 * �ӷ�������ȡ����
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
	 * ��ͼ������
	 */
	private void initListener() {
		
		//ListView��������
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
		
		//ListView��������
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
			//���û�����һ�������󷵻غ�ӷ���������һ��
			getEventsData();
		}
	}

	/**
	 * �����б�������
	 * 
	 * @author: liwei
	 * @Description: ��б�������
	 * @date: 2015��4��16��
	 */
	private class ListAdapter extends BaseAdapter {
		/** ����Դ */
		ArrayList<EventsInfo> objects;
		/** ������ */
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
			// ��ʼ��ֵ
			viewHolder.atvContent.setText(event.eventContent);
			viewHolder.atvPerson.setText(event.personName);
			viewHolder.atvStartTime.setText(event.startTime);

			return convertView;
		}

		class ViewHolder {
			/** ������� */
			TextView atvPerson;
			/** ���ʼʱ�� */
			TextView atvStartTime;
			/** ����� */
			TextView atvContent;
		}

	}

	/**
	 * 
	 * @author: liwei
	 * @Description: ִ���첽����
	 * @date: 2015��4��16��
	 */
	private class TaskHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mDataAdapter = new ListAdapter(mContext, mEventsList);
				mEventsListView.setAdapter(mDataAdapter);
				// ˢ�½���
				mEventsListView.onRefreshComplete();
				break;
			case 1:
				//ɾ���ɹ���ӷ�������ȡһ������
				mProgressDele.dismiss();
				boolean isSuccess = (Boolean) msg.obj;
				//���ɾ���ɹ���ˢ���б�
				if(isSuccess){
					getEventsData();
					Toast.makeText(mContext, "ɾ���ɹ�", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(mContext, "ɾ��ʧ�ܣ���������״̬", Toast.LENGTH_LONG).show();
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
