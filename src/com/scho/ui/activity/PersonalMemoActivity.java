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
 * @Description:  ���˱���¼ 
 * @date:  2015��4��20��
 */
public class PersonalMemoActivity extends Activity{
    /** ������*/
	private Context mContext;
	/** �����б�*/
	private NavigationListView mListView;
	/** ����Դ*/
	private ArrayList<MemoInfo> mDataSource = new ArrayList<MemoInfo>();
	/** ������*/
	private ListAdapter mDataAdapter;
	/** �첽����*/
	private TaskHandler mHandler;
	/** ɾ������*/
	private ProgressDialog mProgressDele;
	 /** �����͵���ѡ�еı���¼*/
		private MemoInfo mSelMemoInfo;
    /** ��ǰ����item����*/
		private int mOldItemCount;
		/** ��ҳ�������*/
		private int mCurItemCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_memo);
		mOldItemCount = getIntent().getIntExtra("item_count",-1);
		mContext = this;
		mListView = (NavigationListView) findViewById(R.id.personal_memo);
		
		mProgressDele = new ProgressDialog(mContext);
		mProgressDele.setMessage("����ɾ��...");
		
		mHandler = new TaskHandler();
		getDataFromServer();
		mListView.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// ���￪�����߳�ִ����������
				getDataFromServer();
			}
		});
		
		//ΪListViewע�᳤���¼�
	registerForContextMenu(mListView);
	}
	 @Override  
	    public void onCreateContextMenu(ContextMenu menu, View v,  
	            ContextMenuInfo menuInfo) {  
	            menu.setHeaderTitle("����¼ɾ��");  
	            //��Ӳ˵���  
	            menu.add(0,Menu.FIRST,0,"ɾ��");  
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
				//ɾ��ѡ�е�
				mProgressDele.show();
			    new Thread(new Runnable(){

					@Override
					public void run() {
						Message msg = mHandler.obtainMessage();
						msg.what = 1;
						//��Ϊ�վ�ɾ��
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
	 * @Description: ִ���첽����
	 * @date: 2015��4��16��
	 */
	private class TaskHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mDataAdapter = new ListAdapter(mContext, mDataSource);
				mListView.setAdapter(mDataAdapter);
				mCurItemCount = mDataSource.size();
				// ˢ�½���
				mListView.onRefreshComplete();
				break;
			case 1:
				mProgressDele.dismiss();
				boolean isSuccess = (Boolean) msg.obj;
				if(isSuccess){
					getDataFromServer();
					Toast.makeText(mContext, "ɾ���ɹ�", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(mContext, "ɾ��ʧ�ܣ���������״̬", Toast.LENGTH_LONG).show();
				}
			default:
				break;
			}
		}
	}
	/**
	 * �ӷ�������ȡ����
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
	 * �����б�������
	 * 
	 * @author: liwei
	 * @Description: ��б�������
	 * @date: 2015��4��16��
	 */
	private class ListAdapter extends BaseAdapter {
		/** ����Դ */
		ArrayList<MemoInfo> objects;
		/** ������ */
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
			// ��ʼ��ֵ
			viewHolder.createTime.setText(memoInfo.createTime);
			viewHolder.content.setText(memoInfo.content);

			return convertView;
		}

		class ViewHolder {
			/** ����ʱ�� */
			TextView createTime;
			/** ����¼���� */
			TextView content;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==14 && resultCode ==15){
			//���û�����һ������¼�󷵻غ�ӷ���������һ��
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
		menu.add(1, 1, 0, "�½�����¼");
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
