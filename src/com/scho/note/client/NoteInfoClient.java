/**
 * 
 */
package com.scho.note.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.scho.note.Constants;
import com.scho.note.entity.NoteInfo;
import com.scho.note.entity.UserInfo;
import com.scho.note.utils.NoteUtil;

/**
 * @author ��ΰ
 * @Description:TODO 
 * @date:2015��4��4�� ����8:53:40
 */

public class NoteInfoClient  extends BaseHelperClient{
	
	/**
	 * ��ȡ��ҳ����Ϣ
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��4��4�� ����4:42:31
	 * @param itemNum  ����
	 * @param uid  ��ǰ�û�uid�����ڲ��ұʼ�����ڵ��û���Ϣ
	 * @return
	 */
	public static ArrayList<NoteInfo> getHomeNoteDatafromServer(int itemNum,String uid){
		ArrayList<NoteInfo> mNoteItemLists = new ArrayList<NoteInfo>();
		//��ȡ��ǰ�û�
		UserInfo userInfo = getUserInfoFromServer(uid);
		NoteInfo noteInfo;
			AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.TableName.TABLE_NOTE);
			//�ʼ�����
			int itemCount = 0;
			//ֻ��ˢ�µ�ʱ���ȡ���������ظ��಻��
			try {
				//���Ǻֱܴ��Ĵ���ÿ�μ��ظ��඼���ȡ��������û��Ҫ��ֻ��ˢ�²��ұ�Ҫ��ȡ����
				//��ˣ�Ӧ�ý�ˢ�ºͼ��ظ����������������
				itemCount = query.count();
				//���������棬����������õ���������ж��������ܷ���ظ���
			} catch (AVException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//����ʱ�併���ѯ�����µĻ�չʾ�ڵ�һ��
			query.orderByDescending("createdAt");
			//��ѯ����
			if(itemNum >= itemCount){
				itemNum = itemCount;
			}
//			query.limit(itemNum);
			//��ҳ��ѯ����˼���Ǻ���ǰitemNum����������ǵ�һ�ν�����������ˢ�¾Ͳ�Ҫ����,ͬ��
			if(itemNum != 0 && itemNum <= itemCount){
				query.setSkip(itemNum);
			}
			//ÿ�β�ѯ������¼
			query.setLimit(5);
			try {
			    List<AVObject> avObjects = query.find();
			    for(int i=0;i<avObjects.size();i++){
			        AVObject avoB = avObjects.get(i);
			        noteInfo = new NoteInfo();
			        //��ȡ�û�
			        noteInfo.userId= avoB.getString("uid");
			        //��ȡ���������
			        noteInfo.noteContent = avoB.getString("Content");
			        //�ʼ�id
			        noteInfo.noteId = avoB.getString("noteId");
			        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd  hh:ss");
			        Date createDate = avoB.getCreatedAt();
			        //�����ʱ��
			        String noteTime = "";
			        if(createDate !=null){
			        	noteInfo.noteCreateTime = format.format(createDate);
			        }
			        //��ȡ�����ͼƬ
			        AVFile avFile = avoB.getAVFile("noteImage");
			        if(avFile !=null){
			        	//�ʼ�ͼƬ��ַ
			        	noteInfo.noteImageUrl = avFile.getUrl();
			        }
			        //���޵Ĵ���
			        noteInfo.zan =avoB.getString("zan");
			        //������
			        noteInfo.noteComment = getCommentCount(noteInfo.noteId);
			        if (userInfo != null) {
			        	//��ȡ�û�ͷ���ַ
			        	noteInfo.userImageUrl = userInfo.noteUserImage;
			        	//��ȡ�û��ǳ�
			        	noteInfo.userName = userInfo.userName;
					}
			        
			    	mNoteItemLists.add(noteInfo);
			    }
			} catch (AVException e) {
			    Log.d("ʧ��", "��ѯ����: " + e.getMessage());
			}
		return mNoteItemLists;
	}
	
	/**
	 * ����uid��ȡ���û�����������ռ��б�
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��4��3�� ����10:16:19
	 * @param uid
	 * @return
	 */
	public static ArrayList<NoteInfo> getRecordDatafromServer(String uid){
		ArrayList<NoteInfo> mNoteItemLists = new ArrayList<NoteInfo>();
		NoteInfo noteInfo;
		//��ȡ��ǰ�û�
		UserInfo userInfo = UserInfoClient.getUserInfoFromServer(uid);
			AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.TableName.TABLE_NOTE);
			//��ѯ��uid�˺��µķ�����¼
			query.whereEqualTo("uid", uid);
			query.addAscendingOrder("createdAt");
			try {
			    List<AVObject> avObjects = query.find();
			    for(int i=0;i<avObjects.size();i++){
			        AVObject avoB = avObjects.get(i);
			        noteInfo = new NoteInfo();
			        //��ȡ�û�
			        noteInfo.userId= avoB.getString("uid");
			        //��ȡ���������
			        noteInfo.noteContent = avoB.getString("Content");
			        //�ʼ�id
			        noteInfo.noteId = avoB.getString("noteId");
			        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd  hh:ss");
			        Date createDate = avoB.getCreatedAt();
			        //�����ʱ��
			        String noteTime = "";
			        if(createDate !=null){
			        	noteInfo.noteCreateTime = format.format(createDate);
			        }
			        //��ȡ�����ͼƬ
			        AVFile avFile = avoB.getAVFile("noteImage");
			        if(avFile !=null){
			        	//�ʼ�ͼƬ��ַ
			        	noteInfo.noteImageUrl = avFile.getUrl();
			        }
			        //���޵Ĵ���
			        noteInfo.zan =avoB.getString("zan");
			        //������
			        noteInfo.noteComment = getCommentCount(noteInfo.noteId);
			        if (userInfo != null) {
			        	//��ȡ�û�ͷ���ַ
			        	noteInfo.userImageUrl = userInfo.noteUserImage;
			        	//��ȡ�û��ǳ�
			        	noteInfo.userName = userInfo.userName;
					}
			        
			    	mNoteItemLists.add(noteInfo);
			    	
			    }
			} catch (AVException e) {
			    Log.d("ʧ��", "��ѯ����: " + e.getMessage());
			}
		return mNoteItemLists;
	}
	/**
	 * ����noteid��ѯ�ñʼ����۵�����
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��4��4�� ����10:50:54
	 * @param noteId
	 * @return
	 */
	public static int getCommentCount(String noteId){
		AVQuery<AVObject> query = AVQuery.getQuery(Constants.TableName.TABLE_NOTE_NOTECOMMENT);
		query.whereEqualTo("noteId", noteId);
		int count = 0;
		try {
			count = query.count();
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
}
