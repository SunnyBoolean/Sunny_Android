/**
 * 
 */
package com.scho.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.scho.Constants;
import com.scho.client.SchoolInfoClient;
import com.scho.ui.R;
import com.scho.ui.activity.PersonalMemoActivity;
import com.scho.ui.activity.PersonalScheduleActivity;

/**
 * @author: liwei
 * @Description: ����������Ƭ
 * @date: 2015��4��16��
 */
public class PersonalCenterFragment extends Fragment {
	/** ������ */
	private Context mContext;
	/** ���˿α� */
	private TextView mPersonalClass;
	/** ����¼ */
	private TextView mPersonalRem;
	/** �ճ̰��� */
	private TextView mPersonalClian;
	/** �μӵ����� */
	private TextView mPersonalGroup;
	/** �ҵĽ��� */
	private TextView mPersonalHoner;
	/** ���� */
	private TextView mPersonalMore;
	/** ����¼���ܼ�¼��*/
	private int mMemoItemCount;
	/** �첽����*/
	private TaskHandler mHandler;
	/** ��ǰ�û�*/
	private AVUser mCurUser = AVUser.getCurrentUser();
	/** ��ǰ�û���*/
	private TextView mUserText;
	
	
	private static PersonalCenterFragment mInstance;

	// ��ȡ��ҳ��Ƭʵ��
	public static PersonalCenterFragment getInstance() {
		if (mInstance == null) {
			mInstance = new PersonalCenterFragment();
		}
		return mInstance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		mContext = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_personalcenter, null);
		mPersonalClass = (TextView) view.findViewById(R.id.personal_class);
		mPersonalClian = (TextView) view.findViewById(R.id.personal_clian);
		mPersonalGroup = (TextView) view.findViewById(R.id.personal_group);
		mPersonalHoner = (TextView) view.findViewById(R.id.personal_honer);
		mPersonalMore = (TextView) view.findViewById(R.id.personal_more);
		mPersonalRem = (TextView) view.findViewById(R.id.personal_rem);
		mUserText = (TextView) view.findViewById(R.id.person_user_name);
		
		if(mCurUser !=null){
			mUserText.setText(mCurUser.getUsername());
		}
		mHandler = new TaskHandler();
		getMemoData();
		initListener();
		return view;
	}
	/**
	 * ��ȡ����¼����
	 */
	private void getMemoData(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				mMemoItemCount = SchoolInfoClient.getTableItemCount(Constants.SCHO_PERSONLA_MEMO);
				mHandler.sendEmptyMessage(0);
			}
			
		}).start();
	}
	/**
	 * �첽����
	 * @author:  hello
	 * @Description:  TODO 
	 * @date:  2015��4��23��
	 */
	private class TaskHandler extends Handler{
	  @Override
	  public void handleMessage(Message msg){
		  mPersonalRem.setText("����¼("+mMemoItemCount+")");
	  }
	}
	/**
	 * �����¼�����
	 */
	private void initListener() {
		ClickListener listener = new ClickListener();
		mPersonalClass.setOnClickListener(listener);
		mPersonalClian.setOnClickListener(listener);
		mPersonalGroup.setOnClickListener(listener);
		mPersonalHoner.setOnClickListener(listener);
		mPersonalMore.setOnClickListener(listener);
		mPersonalRem.setOnClickListener(listener);
	}
  @Override
  public void onActivityResult(int requestCode,int resultCode,Intent data){
	  super.onActivityResult(requestCode, resultCode, data);
	  
	  if(requestCode ==18 && resultCode ==19 && data!=null ){
		  int newItemCount = data.getIntExtra("cur_count",-1);
		  //����ܼ�¼��û��Ͳ�ˢ�·���
		  if(mMemoItemCount != newItemCount){
			  getMemoData();
		  }
	  }
  }
	/**
	 * 
	 * @author: liwei
	 * @Description: �����¼�������
	 * @date: 2015��4��20��
	 */
	private class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//���˿α�
			case R.id.personal_class:
                 Toast.makeText(mContext, "�˹�����δ����", Toast.LENGTH_LONG).show();
				break;
				//�ճ̰���
			case R.id.personal_clian:
                Intent intent = new Intent();
                intent.setClass(mContext, PersonalScheduleActivity.class);
                startActivity(intent);
				break;
				//����¼
			case R.id.personal_rem:
				Intent intents = new Intent();
                intents.setClass(mContext, PersonalMemoActivity.class);
                intents.putExtra("item_count", mMemoItemCount);
                startActivityForResult(intents, 18);
				break;
				//�ҵ�����
			case R.id.personal_group:
				 Toast.makeText(mContext, "�˹�����δ����", Toast.LENGTH_LONG).show();
				break;
				//��������
			case R.id.personal_honer:
				 Toast.makeText(mContext, "�˹�����δ����", Toast.LENGTH_LONG).show();
				break;
				//����
			case R.id.personal_more:
				 Toast.makeText(mContext, "û�и��๦��", Toast.LENGTH_LONG).show();
				break;
			
			default:
				break;
			}
		}

	}

}
