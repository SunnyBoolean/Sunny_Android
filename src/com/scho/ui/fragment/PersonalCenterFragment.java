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
 * @Description: 个人中心碎片
 * @date: 2015年4月16日
 */
public class PersonalCenterFragment extends Fragment {
	/** 上下文 */
	private Context mContext;
	/** 个人课表 */
	private TextView mPersonalClass;
	/** 备忘录 */
	private TextView mPersonalRem;
	/** 日程安排 */
	private TextView mPersonalClian;
	/** 参加的社团 */
	private TextView mPersonalGroup;
	/** 我的奖励 */
	private TextView mPersonalHoner;
	/** 更多 */
	private TextView mPersonalMore;
	/** 备忘录的总记录数*/
	private int mMemoItemCount;
	/** 异步任务*/
	private TaskHandler mHandler;
	/** 当前用户*/
	private AVUser mCurUser = AVUser.getCurrentUser();
	/** 当前用户名*/
	private TextView mUserText;
	
	
	private static PersonalCenterFragment mInstance;

	// 获取首页碎片实例
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
	 * 获取备忘录条数
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
	 * 异步任务
	 * @author:  hello
	 * @Description:  TODO 
	 * @date:  2015年4月23日
	 */
	private class TaskHandler extends Handler{
	  @Override
	  public void handleMessage(Message msg){
		  mPersonalRem.setText("备忘录("+mMemoItemCount+")");
	  }
	}
	/**
	 * 单击事件监听
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
		  //如果总记录数没变就不刷新服务
		  if(mMemoItemCount != newItemCount){
			  getMemoData();
		  }
	  }
  }
	/**
	 * 
	 * @author: liwei
	 * @Description: 单击事件监听器
	 * @date: 2015年4月20日
	 */
	private class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//个人课表
			case R.id.personal_class:
                 Toast.makeText(mContext, "此功能暂未开放", Toast.LENGTH_LONG).show();
				break;
				//日程安排
			case R.id.personal_clian:
                Intent intent = new Intent();
                intent.setClass(mContext, PersonalScheduleActivity.class);
                startActivity(intent);
				break;
				//备忘录
			case R.id.personal_rem:
				Intent intents = new Intent();
                intents.setClass(mContext, PersonalMemoActivity.class);
                intents.putExtra("item_count", mMemoItemCount);
                startActivityForResult(intents, 18);
				break;
				//我的社团
			case R.id.personal_group:
				 Toast.makeText(mContext, "此功能暂未开放", Toast.LENGTH_LONG).show();
				break;
				//个人荣誉
			case R.id.personal_honer:
				 Toast.makeText(mContext, "此功能暂未开放", Toast.LENGTH_LONG).show();
				break;
				//更多
			case R.id.personal_more:
				 Toast.makeText(mContext, "没有更多功能", Toast.LENGTH_LONG).show();
				break;
			
			default:
				break;
			}
		}

	}

}
