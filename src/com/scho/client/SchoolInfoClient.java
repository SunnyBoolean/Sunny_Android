/**
 * 
 */
package com.scho.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SaveCallback;
import com.scho.Constants;
import com.scho.entity.EventsInfo;
import com.scho.entity.MemoInfo;
import com.scho.utils.NoteUtil;

/**
 * @author:  liwei
 * @Description:  �ӷ�������ȡѧУ��Ϣ�࣬������ȡ�羰ͼƬ��ѧУ����
 * @date:  2015��4��16��
 */
public class SchoolInfoClient {
    /** �羰ͼƬurl*/
	private static ArrayList<String> schoolImage = new ArrayList<String>();
	
	public static ArrayList<String> getSchoolImage(){
		//��ѯ�ļ���
		AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.SCHOL_PIC);
		List<AVObject> avoList = new ArrayList<AVObject>();
		int count = 0;
		try {
			count = query.count();
			avoList = query.find();
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<count;i++){
			AVObject avo = avoList.get(i);
			AVFile avF = avo.getAVFile("name");
			String picUrl = avF.getUrl();
			schoolImage.add(picUrl);
		}
		return schoolImage;
	}
	/**
	 * ��ȡ���Ϣ
	 * @return
	 */
	public static ArrayList<EventsInfo> getEventsClient(){
		ArrayList<EventsInfo> list = new ArrayList<EventsInfo>();
		AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.SCHO_EVENTS);
//		query.whereEqualTo("noteId", noteId);
		//ʱ�併������
		query.orderByDescending("createdAt");
		List<AVObject> objects = null;
			try {
				objects = query.find();
				if(objects !=null){
					EventsInfo info;
				for(int i=0;i<objects.size();i++){
					AVObject obj = objects.get(i);
					info = new EventsInfo();
					info.endTime = obj.getString("act_endtime");
					info.eventLocation = obj.getString("act_location");
					info.eventName = obj.getString("act_theme");
					info.eventContent = obj.getString("act_content");
					info.personName = obj.getString("act_person");
					info.startTime = obj.getString("act_starttime");
					info.eventsId = obj.getObjectId();
					Date date = obj.getCreatedAt();
//					info.startTime = NoteUtil.gelinTimeToChinaTime(date);
					list.add(info);	
					
				}
				}
			} catch (AVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return list;
	}
	/**
	 * �ϴ��½��
	 * @param info  �����
	 * @return  true��ʾ�ϴ��ɹ�  falseʧ��
	 */
	public static HttpResultInfo uploadNewEvent(EventsInfo info,final Context context){
		final HttpResultInfo result = new HttpResultInfo();
		AVObject obj = new AVObject(Constants.SCHO_EVENTS);
		//�id��Ψһ��ʶ
		obj.put("act_id", NoteUtil.getId());
		//�������
		obj.put("act_person", info.personName);
		//���ʼʱ��
		obj.put("act_starttime", info.startTime);
		//�����ʱ��
		obj.put("act_endtime", info.endTime);
		//�����
		obj.put("act_name", info.eventName);
		//�����
		obj.put("act_content", info.eventContent);
		//��ص�
		obj.put("act_location", info.eventLocation);
		
		obj.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(AVException arg0) {
				if(arg0==null){
					result.success = true;
					result.resultInfo = "��½��ϴ��ɹ���";
					Toast.makeText(context, "�ϴ��ɹ���", Toast.LENGTH_LONG).show();
				}else{
					result.success = false;
					result.resultInfo = "�ϴ�ʧ�ܣ��������磡";
					Toast.makeText(context, "�ϴ�ʧ�ܣ�", Toast.LENGTH_LONG).show();
				}
			}
		});
		return result;
	}
	/**
	 * ��ȡ����¼��Ϣ
	 * @return
	 */
	public static ArrayList<MemoInfo> getMemoClient(){
		ArrayList<MemoInfo> list = new ArrayList<MemoInfo>();
		AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.SCHO_PERSONLA_MEMO);
//		query.whereEqualTo("noteId", noteId);
		//ʱ�併������
		query.orderByDescending("createdAt");
		List<AVObject> objects = null;
			try {
				objects = query.find();
				if(objects !=null){
					MemoInfo info;
				for(int i=0;i<objects.size();i++){
					AVObject obj = objects.get(i);
					info = new MemoInfo();
					//��ȡ����
					info.content = obj.getString("memo_content");
					//����¼id
					info.memoId = obj.getObjectId();
					//ʱ��
					Date date = obj.getCreatedAt();
					info.createTime = NoteUtil.gelinTimeToChinaTime(date);
					list.add(info);	
					
				}
				}
			} catch (AVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return list;
	}
	
	/**
	 * �ϴ��½�����¼
	 * @param info  �½�������
	 * @param context
	 * @return
	 */
	public static HttpResultInfo uploadNewMemo(MemoInfo info,final Context context){
		final HttpResultInfo result = new HttpResultInfo();
		AVObject obj = new AVObject(Constants.SCHO_PERSONLA_MEMO);
		//����¼id��Ψһ��ʶ
		obj.put("memo_id", NoteUtil.getId());
		//����¼����
		obj.put("memo_content", info.content);
		obj.saveInBackground(new SaveCallback() {
			@Override
			public void done(AVException arg0) {
				if(arg0==null){
					result.success = true;
					result.resultInfo = "����¼�½��ϴ��ɹ���";
					Toast.makeText(context, "�ϴ��ɹ���", Toast.LENGTH_LONG).show();
				}else{
					result.success = false;
					result.resultInfo = "�ϴ�ʧ�ܣ��������磡";
					Toast.makeText(context, "�ϴ�ʧ�ܣ�", Toast.LENGTH_LONG).show();
				}
			}
		});
		return result;
	}
	/**
	 * ��ȡָ������ܼ�¼��
	 * @return
	 */
	public static int getTableItemCount(String table){
		AVQuery<AVObject> query = new AVQuery<AVObject>(table);
		int count = 0;
		try {
			count = query.count();
		} catch (AVException e) {
			e.printStackTrace();
		}
	    return count;
	}
	
	
	/**
	 * ɾ�����¼����Ҫ�������¼������¼��¼
	 * @param itemId  ��¼id
	 * @param tableName  ������
	 * @return  true��ʾɾ���ɹ�
	 */
	public static boolean deleteEvents(String itemId,String tableName){
		//��ѯָ������
		AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
		AVObject obj;
		try {
			//���Ҷ���
			obj = query.get(itemId);
			if(obj !=null){
				//Ȼ��ɾ��֮
				obj.delete();
				return true;
			}
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		return false;
	}
	
}
