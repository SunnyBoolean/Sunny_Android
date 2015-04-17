/**
 * 
 */
package com.scho.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract.EventsEntity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.scho.Constants;
import com.scho.client.SchoolInfoClient;
import com.scho.entity.EventsInfo;
import com.scho.ui.R;
import com.scho.widget.NavigationListView;
import com.scho.widget.NavigationListView.OnRefreshListener;

/**
 * @author:  liwei
 * @Description:  TODO 
 * @date:  2015��4��16��
 */
public class SchoolEventsFragment extends Fragment{
	/** ������*/
	private Context mContext;
     /** ��Ƭʵ��*/
	 private static SchoolEventsFragment mInstance;
	 /** ListView������*/
	 private NavigationListView mEventsListView;
	 /** ����������*/
	 private ListAdapter mDataAdapter;
	 /** ����Դ*/
	 private ArrayList<EventsInfo> mEventsList;
	 /** �첽������*/
	 private TaskHandler mHandler;
	 
	    //��ȡ��ҳ��Ƭʵ��
	    public static SchoolEventsFragment getInstance(){
	    	if(mInstance == null){
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
			super.onAttach(activity);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.scho_event_list, null);
			mEventsListView = (NavigationListView) view.findViewById(R.id.scho_events);
			mHandler = new TaskHandler();
			mEventsListView.setonRefreshListener(new OnRefreshListener() {  
	            @Override  
	            public void onRefresh() {  
	            	//���￪�����߳�ִ����������
	            	getEventsData();
	            }  
	        });  
			return view;
		}
		/**
		 * �ӷ�������ȡ����
		 */
		public void getEventsData(){
			new Thread(new Runnable(){
				@Override
				public void run() {
					mEventsList = SchoolInfoClient.getEventsClient();
					Message msg = mHandler.obtainMessage();
					msg.what = 0;
				}
				
			}).start();
		}
		/**
		 * �����б�������
		 * @author:  liwei
		 * @Description:  TODO 
		 * @date:  2015��4��16��
		 */
		private class ListAdapter extends BaseAdapter{
			/**����Դ */
            ArrayList<EventsInfo> objects;
            /** ������*/
            LayoutInflater inflater;
            public ListAdapter(Context context,ArrayList<EventsInfo> objects){
            	this.objects = objects;
            	inflater = LayoutInflater.from(context);
            }
			@Override
			public int getCount() {
				if(objects !=null){
					return objects.size();
				}
				return 0;
			}

			@Override
			public Object getItem(int position) {
				if(objects !=null){
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
				if(convertView==null){
					viewHolder = new ViewHolder();
					convertView = inflater.inflate(R.layout.scho_event_list_item, null);
					viewHolder.atvContent = (TextView) convertView.findViewById(R.id.act_content);
					viewHolder.atvPerson = (TextView) convertView.findViewById(R.id.act_peraon);
					viewHolder.atvStartTime = (TextView) convertView.findViewById(R.id.starttime);
					convertView.setTag(viewHolder);
				}else{
					viewHolder = (ViewHolder) convertView.getTag();
				}
				EventsInfo event = objects.get(position);
				//��ʼ��ֵ
				viewHolder.atvContent.setText(event.eventTheme);
				viewHolder.atvPerson.setText(event.personName);
				viewHolder.atvStartTime.setText(event.startTime);
				
				return convertView;
			}
			class ViewHolder{
				/** �������*/
				TextView atvPerson;
				/** ���ʼʱ��*/
				TextView atvStartTime;
				/** �����*/
				TextView atvContent;
			}
			
		}
		/**
		 * 
		 * @author:  liwei
		 * @Description: ִ���첽���� 
		 * @date:  2015��4��16��
		 */
		private class TaskHandler extends Handler{
			@Override
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					mDataAdapter = new ListAdapter(mContext,mEventsList);
					mEventsListView.setAdapter(mDataAdapter);
					//ˢ�½���
					mEventsListView.onRefreshComplete();
					break;

				default:
					break;
				}
			}
		}
}
