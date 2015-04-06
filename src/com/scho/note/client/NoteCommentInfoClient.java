/**
 * 
 */
package com.scho.note.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVOperationQueue;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.scho.note.Constants;
import com.scho.note.entity.NoteCommentInfo;
import com.scho.note.entity.UserInfo;
import com.scho.note.utils.NoteUtil;

/**
 * @author ��ΰ
 * @Description: �ʼ����ۿͻ������������ͨ����
 * @date:2015��4��4�� ����10:19:19
 */

public class NoteCommentInfoClient extends BaseHelperClient{
    /**
     * ���ݱʼ�id��ȡ�����б�
     * Description: TODO
     * @author: ��ΰ
     * @date:2015��4��4�� ����10:23:57
     * @param noteId
     * @return
     */
	public static ArrayList<NoteCommentInfo> getNoteCommentList(String noteId,final String uid){
		final ArrayList<NoteCommentInfo> commentArr = new ArrayList<NoteCommentInfo>();
		
		AVQuery<AVObject> query = AVQuery.getQuery(Constants.TableName.TABLE_NOTE_NOTECOMMENT);
		//����noteId��ѯnote�µ���������
		query.whereEqualTo("noteId", noteId);
		//ʱ�併������
		query.orderByDescending("createdAt");
		List<AVObject> objects = null;
		try {
			objects = query.find();
            if(objects !=null){
            	NoteCommentInfo noteInfo;
//            	UserInfo userInfo = getUserInfoFromServer(uid);
            	for(int i=0;i<objects.size() && objects.get(i) !=null ;i++){
            		AVObject object = objects.get(i);
            		noteInfo = new NoteCommentInfo();
            		//����id
            		noteInfo.noCommentId = object.getString("commentId");
            		//��������
            		noteInfo.noContent = object.getString("noContent");
            		//�ʼ�id
            		noteInfo.noteId = object.getString("noteId");
            		//�û�id
            		noteInfo.userId = object.getString("userId");
            		//�û�ͷ���ַ
            		noteInfo.userImageUrl = object.getString("userImageUrl");
            		//�û��ǳ�
            		noteInfo.userName = object.getString("userName");
            		//����ʱ��
            		 Date createDate = object.getCreatedAt();
            		 if(createDate !=null){
            			 noteInfo.ntCommentTime = NoteUtil.gelinTimeToChinaTime(createDate);
            		 }
            		
            		commentArr.add(noteInfo);
            	}
            }
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commentArr;
	}
	
	/**
	 * 
	 * Description: �ϴ�������Ϣ��������
	 * @author: ��ΰ
	 * @date:2015��4��5�� ����5:42:31
	 * @param commentInfo
	 */
	public static HttpResultInfo uploadComment(NoteCommentInfo commentInfo){
		HttpResultInfo result = new HttpResultInfo();
		AVObject object = new AVObject(Constants.TableName.TABLE_NOTE_NOTECOMMENT);
		if(commentInfo != null){
			//����id�����ϴ�ʱ�ڿͻ�������һ��id
			String commentId = NoteUtil.getId();
			//��������id
			object.put("commentId",commentId);
			//������������
			object.put("noContent", commentInfo.noContent);
			//����noteid
			object.put("noteId",commentInfo.noteId);
			//����userid
			object.put("userId", commentInfo.userId);
			//�����û�ͷ���ַ
			object.put("userImageUrl", commentInfo.userImageUrl);
			//�����û��ǳ�
			object.put("userName",commentInfo.userName);
			
			/**
			 * �ϴ�
			 */
			try {
				object.save();
			} catch (AVException e) {
				e.printStackTrace();
				result.success = false;
				result.resultInfo = "����ʧ��";
			}
		}
		
			
		
		return result;
	}
	
}
