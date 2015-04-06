/**
 * 
 */
package com.scho.note.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.note.ui.activity.R;
import com.scho.note.Constants;
import com.scho.note.basic.fragment.BaseReportFragment;
import com.scho.note.common.NoteCommon;
import com.scho.note.entity.NoteInfo;
import com.scho.note.sina.AccessTokenKeeper;
import com.scho.note.ui.activity.AddNewNoteActivity;
import com.scho.note.utils.NoteUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * @author liwei
 * @Description:打卡签到，用户在这里对自己之前发表的日记进行追加记录，用来保持持续坚持去做一件事情
 * @date:2015年2月15日 上午1:01:13
 */

public class ReportFragment extends BaseReportFragment{
	/*
	 * 首先进来判断用户是否登陆，
	 * 如果登陆就进行检测是否有发表记录
	 * 如果没有记录就提醒用户发表记录
	 * 如果有就允许用户对相应得目标打卡签到
	 * 如果没有登陆就不用检测是否发表记录了，就无需从服务器或去查询记录
	 */
    /** 文本描述*/    
    private TextView tv;
    /** */
    private Context mContext;
    /** 用户发表的日记记录列表*/
	private ArrayList<NoteInfo> mNoteItemLists;
	/** 保存的token信息*/
   private Oauth2AccessToken  mAccessToken;
   /** 用户id*/
   private String mUid;
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
		// 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        mUid = mAccessToken.getUid();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_test, null);
		tv = (TextView) v.findViewById(R.id.test_tv);
		//如果没有登陆返回的是false
		if(!NoteCommon.checkIsUserLogined(mContext)){
			tv.setText("单击添加记录");
			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("uid", mUid);
			intent.setClass(mContext, AddNewNoteActivity.class);
			startActivity(intent);
				}
			});
//			tv.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					NoteCommon.jumpToLoginMenu(mContext);
//					
//				}
			
//			});
		}else{
			tv.setText("打卡");
		}
		
		return v;
	}

	/**
	 * 检查用户是否发表过日志记录
	 * Description: TODO
	 * @author: 李伟
	 * @date:2015年3月7日 下午2:40:59
	 * @return
	 */
	public boolean checkIsHasNoteRecord(){
		if(false){
			//如果登陆就进行检查是否有记录
			//有
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 从服务器加载用户发帖记录
	 * Description: TODO
	 * @author: 李伟
	 * @date:2015年4月3日 下午10:11:45
	 * @param page
	 */
	private void loadRecordData(int page) {
		mNoteItemLists = new ArrayList<NoteInfo>();
		String url = "";
		if(page <=5){
			AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.TableName.TABLE_NOTE);
//			query.whereEqualTo("playerName", "steve");
			try {
			    List<AVObject> avObjects = query.find();
			    for(int i=0;i<avObjects.size();i++){
			        AVObject avoB = avObjects.get(i);
			        //发表的内容
			        String content = avoB.getString("Content");
			        //发表的图片
			        AVFile avFile = avoB.getAVFile(avoB.getString("noteImage"));
			        String imageUrl = "a.jpg";
			        if(avFile !=null){
			        	imageUrl = avFile.getUrl();
			        }
			    	mNoteItemLists.add(new NoteInfo("12swae3q12","3243","张三",imageUrl,  content,imageUrl, "2015-04-02", 8,"35"));
			    	
			    }
			} catch (AVException e) {
			    Log.d("失败", "查询错误: " + e.getMessage());
			}
		}
	}
}
