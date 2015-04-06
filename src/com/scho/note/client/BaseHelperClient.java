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
 * @author 李伟
 * @Description:客户端与服务器交互的基类
 * @date:2015年3月10日 下午8:35:11
 */

public class BaseHelperClient {
	/**
	 * 根据uid从服务器获取用户信息，由于很多地方都需要用到用户信息，所以将该方法放在基类中
	 * Description: 根据uid获取用户信息
	 * @author: 李伟
	 * @date:2015年4月4日 上午1:22:49
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
		            // 查询成功
		        	AVObject avObj = objects.get(0);
		        	//用户id
		        	userInfo.userId = (String) avObj.get("uid");
		        	//用户头像50*50
		        	userInfo.noteUserImage = (String) avObj.get("noteUserUrl");
		        	//用户昵称
		        	userInfo.userName = (String) avObj.get("username");
//		        	avObj.getAVUser(uid);
		        } else {
		            // 查询出错
		        }
		    }
		});
		return userInfo;
	}
	/**
	 * 输入流转换成字符串
	 * Description: TODO
	 * @author: 李伟
	 * @date:2015年4月3日 上午1:41:10
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
     * Description: 输入流传换成字节数组
     * @author: 李伟
     * @date:2015年4月3日 上午1:38:59
     * @param inStream
     * @return
     * @throws Exception
     */
    public  static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
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
		    // e.getMessage() 捕获的异常信息
		}
	}
}
