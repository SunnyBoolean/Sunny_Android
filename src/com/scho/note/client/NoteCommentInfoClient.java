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
 * @author 李伟
 * @Description: 笔记评论客户端与服务器端通信类
 * @date:2015年4月4日 下午10:19:19
 */

public class NoteCommentInfoClient extends BaseHelperClient{
    /**
     * 根据笔记id获取评论列表
     * Description: TODO
     * @author: 李伟
     * @date:2015年4月4日 下午10:23:57
     * @param noteId
     * @return
     */
	public static ArrayList<NoteCommentInfo> getNoteCommentList(String noteId,final String uid){
		final ArrayList<NoteCommentInfo> commentArr = new ArrayList<NoteCommentInfo>();
		
		AVQuery<AVObject> query = AVQuery.getQuery(Constants.TableName.TABLE_NOTE_NOTECOMMENT);
		//根据noteId查询note下的所有评论
		query.whereEqualTo("noteId", noteId);
		//时间降序排序，
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
            		//评论id
            		noteInfo.noCommentId = object.getString("commentId");
            		//评论内容
            		noteInfo.noContent = object.getString("noContent");
            		//笔记id
            		noteInfo.noteId = object.getString("noteId");
            		//用户id
            		noteInfo.userId = object.getString("userId");
            		//用户头像地址
            		noteInfo.userImageUrl = object.getString("userImageUrl");
            		//用户昵称
            		noteInfo.userName = object.getString("userName");
            		//评论时间
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
	 * Description: 上传评论信息到服务器
	 * @author: 李伟
	 * @date:2015年4月5日 下午5:42:31
	 * @param commentInfo
	 */
	public static HttpResultInfo uploadComment(NoteCommentInfo commentInfo){
		HttpResultInfo result = new HttpResultInfo();
		AVObject object = new AVObject(Constants.TableName.TABLE_NOTE_NOTECOMMENT);
		if(commentInfo != null){
			//评论id，在上传时在客户端生成一个id
			String commentId = NoteUtil.getId();
			//保存评论id
			object.put("commentId",commentId);
			//保存评论内容
			object.put("noContent", commentInfo.noContent);
			//保存noteid
			object.put("noteId",commentInfo.noteId);
			//保存userid
			object.put("userId", commentInfo.userId);
			//保存用户头像地址
			object.put("userImageUrl", commentInfo.userImageUrl);
			//保存用户昵称
			object.put("userName",commentInfo.userName);
			
			/**
			 * 上传
			 */
			try {
				object.save();
			} catch (AVException e) {
				e.printStackTrace();
				result.success = false;
				result.resultInfo = "评论失败";
			}
		}
		
			
		
		return result;
	}
	
}
