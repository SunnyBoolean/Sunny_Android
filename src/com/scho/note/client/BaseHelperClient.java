/**
 * 
 */
package com.scho.note.client;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import android.provider.ContactsContract.CommonDataKinds.Note;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.scho.note.Constants;
import com.scho.note.entity.NoteCommentInfo;
import com.scho.note.entity.NoteInfo;
import com.scho.note.entity.UserInfo;

/**
 * @author ��ΰ
 * @Description:�ͻ���������������Ļ���
 * @date:2015��3��10�� ����8:35:11
 */

public class BaseHelperClient {
	/**
	 * ����uid�ӷ�������ȡ�û���Ϣ�����ںܶ�ط�����Ҫ�õ��û���Ϣ�����Խ��÷������ڻ�����
	 * Description: ����uid��ȡ�û���Ϣ
	 * @author: ��ΰ
	 * @date:2015��4��4�� ����1:22:49
	 * @param uid
	 * @return
	 */
	public static UserInfo getUserInfoFromServer(String uid){
		final UserInfo userInfo = new UserInfo();
		AVQuery<AVObject> query = AVQuery.getQuery(Constants.TableName.TABLE_USER);
		query.whereEqualTo("uid", uid);
		query.findInBackground(new FindCallback<AVObject>() {
		    public void done(List<AVObject> objects, AVException e) {
		        if (e == null) {
		            // ��ѯ�ɹ�
		        	AVObject avObj = objects.get(0);
		        	//�û�id
		        	userInfo.userId = (String) avObj.get("uid");
		        	//�û�ͷ��50*50
		        	userInfo.noteUserImage = (String) avObj.get("noteUserUrl");
		        	//�û��ǳ�
		        	userInfo.userName = (String) avObj.get("username");
//		        	avObj.getAVUser(uid);
		        } else {
		            // ��ѯ����
		        }
		    }
		});
		return userInfo;
	}
	/**
	 * ������ת�����ַ���
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��4��3�� ����1:41:10
	 * @param is
	 * @return
	 */
    public static String inStreamToString(InputStream is){
    	String str = "";
    	if(is != null){
    		try {
				str = new String(readInputStream(is));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return str;
    }
    /**
     * 
     * Description: �������������ֽ�����
     * @author: ��ΰ
     * @date:2015��4��3�� ����1:38:59
     * @param inStream
     * @return
     * @throws Exception
     */
    public  static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //����һ��Buffer�ַ���  
        byte[] buffer = new byte[1024];  
        //ÿ�ζ�ȡ���ַ������ȣ����Ϊ-1������ȫ����ȡ���  
        int len = 0;  
        //ʹ��һ����������buffer������ݶ�ȡ����  
        while( (len=inStream.read(buffer)) != -1 ){  
            //���������buffer��д�����ݣ��м����������ĸ�λ�ÿ�ʼ����len�����ȡ�ĳ���  
            outStream.write(buffer, 0, len);  
        }  
        //�ر�������  
        inStream.close();  
        //��outStream�������д���ڴ�  
        return outStream.toByteArray();  
    }  
	
	
	public static void uploadToServer(NoteInfo note) {
		final boolean flag = true;
		AVObject avoNote = new AVObject(Constants.TableName.TABLE_NOTE);
		avoNote.put("Content", note.noteContent);
		avoNote.put("noteId", note.noteId);
		avoNote.put("userId", note.userId);
		avoNote.put("userName", note.userName);
		avoNote.put("noteTitle", note.noteTitle);
		avoNote.put("noteImageUrl", note.noteImageUrl);
		avoNote.put("noteCreateTime", note.noteCreateTime);
		avoNote.put("noteColcaluations", note.noteColcaluations);
		avoNote.put("noteReportCount", note.noteReportCount);
		avoNote.put("noteComment", note.noteComment);
		avoNote.put("isPublic", note.isPublic);
		try {
			avoNote.save();
		} catch (AVException e) {
		    // e.getMessage() ������쳣��Ϣ
		}
	}
}
