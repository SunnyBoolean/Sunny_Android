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
 * @Description:��ǩ�����û���������Լ�֮ǰ������ռǽ���׷�Ӽ�¼���������ֳ������ȥ��һ������
 * @date:2015��2��15�� ����1:01:13
 */

public class ReportFragment extends BaseReportFragment{
	/*
	 * ���Ƚ����ж��û��Ƿ��½��
	 * �����½�ͽ��м���Ƿ��з����¼
	 * ���û�м�¼�������û������¼
	 * ����о������û�����Ӧ��Ŀ���ǩ��
	 * ���û�е�½�Ͳ��ü���Ƿ񷢱��¼�ˣ�������ӷ�������ȥ��ѯ��¼
	 */
    /** �ı�����*/    
    private TextView tv;
    /** */
    private Context mContext;
    /** �û�������ռǼ�¼�б�*/
	private ArrayList<NoteInfo> mNoteItemLists;
	/** �����token��Ϣ*/
   private Oauth2AccessToken  mAccessToken;
   /** �û�id*/
   private String mUid;
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
		// ��ȡ��ǰ�ѱ������ Token
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        mUid = mAccessToken.getUid();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_test, null);
		tv = (TextView) v.findViewById(R.id.test_tv);
		//���û�е�½���ص���false
		if(!NoteCommon.checkIsUserLogined(mContext)){
			tv.setText("������Ӽ�¼");
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
			tv.setText("��");
		}
		
		return v;
	}

	/**
	 * ����û��Ƿ񷢱����־��¼
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��3��7�� ����2:40:59
	 * @return
	 */
	public boolean checkIsHasNoteRecord(){
		if(false){
			//�����½�ͽ��м���Ƿ��м�¼
			//��
			return true;
		}else{
			return false;
		}
	}
	/**
	 * �ӷ����������û�������¼
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��4��3�� ����10:11:45
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
			        //���������
			        String content = avoB.getString("Content");
			        //�����ͼƬ
			        AVFile avFile = avoB.getAVFile(avoB.getString("noteImage"));
			        String imageUrl = "a.jpg";
			        if(avFile !=null){
			        	imageUrl = avFile.getUrl();
			        }
			    	mNoteItemLists.add(new NoteInfo("12swae3q12","3243","����",imageUrl,  content,imageUrl, "2015-04-02", 8,"35"));
			    	
			    }
			} catch (AVException e) {
			    Log.d("ʧ��", "��ѯ����: " + e.getMessage());
			}
		}
	}
}
