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
 * @author 李伟
 * @Description:TODO 
 * @date:2015年4月4日 下午8:53:40
 */

public class NoteInfoClient  extends BaseHelperClient{
	
	/**
	 * 获取首页的信息
	 * Description: TODO
	 * @author: 李伟
	 * @date:2015年4月4日 下午4:42:31
	 * @param itemNum  条数
	 * @param uid  当前用户uid，用于查找笔记相对于的用户信息
	 * @return
	 */
	public static ArrayList<NoteInfo> getHomeNoteDatafromServer(int itemNum,String uid){
		ArrayList<NoteInfo> mNoteItemLists = new ArrayList<NoteInfo>();
		//获取当前用户
		UserInfo userInfo = getUserInfoFromServer(uid);
		NoteInfo noteInfo;
			AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.TableName.TABLE_NOTE);
			//笔记总数
			int itemCount = 0;
			//只在刷新的时候获取总数，加载更多不用
			try {
				//这是很粗暴的处理，每次加载更多都会获取总数，这没必要，只有刷新才右臂要获取总数
				//因此，应该将刷新和加载更多分两个方法来做
				itemCount = query.count();
				//将总数保存，在主界面会用到，会根据判断总数是能否加载更多
			} catch (AVException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//按照时间降序查询，最新的会展示在第一条
			query.orderByDescending("createdAt");
			//查询条数
			if(itemNum >= itemCount){
				itemNum = itemCount;
			}
//			query.limit(itemNum);
			//分页查询，意思就是忽略前itemNum个数，如果是第一次进来，或者是刷新就不要限制,同事
			if(itemNum != 0 && itemNum <= itemCount){
				query.setSkip(itemNum);
			}
			//每次查询五条记录
			query.setLimit(5);
			try {
			    List<AVObject> avObjects = query.find();
			    for(int i=0;i<avObjects.size();i++){
			        AVObject avoB = avObjects.get(i);
			        noteInfo = new NoteInfo();
			        //获取用户
			        noteInfo.userId= avoB.getString("uid");
			        //获取发表的内容
			        noteInfo.noteContent = avoB.getString("Content");
			        //笔记id
			        noteInfo.noteId = avoB.getString("noteId");
			        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd  hh:ss");
			        Date createDate = avoB.getCreatedAt();
			        //发表的时间
			        String noteTime = "";
			        if(createDate !=null){
			        	noteInfo.noteCreateTime = format.format(createDate);
			        }
			        //获取发表的图片
			        AVFile avFile = avoB.getAVFile("noteImage");
			        if(avFile !=null){
			        	//笔记图片地址
			        	noteInfo.noteImageUrl = avFile.getUrl();
			        }
			        //被赞的次数
			        noteInfo.zan =avoB.getString("zan");
			        //评论数
			        noteInfo.noteComment = getCommentCount(noteInfo.noteId);
			        if (userInfo != null) {
			        	//获取用户头像地址
			        	noteInfo.userImageUrl = userInfo.noteUserImage;
			        	//获取用户昵称
			        	noteInfo.userName = userInfo.userName;
					}
			        
			    	mNoteItemLists.add(noteInfo);
			    }
			} catch (AVException e) {
			    Log.d("失败", "查询错误: " + e.getMessage());
			}
		return mNoteItemLists;
	}
	
	/**
	 * 根据uid获取该用户发表的所有日记列表
	 * Description: TODO
	 * @author: 李伟
	 * @date:2015年4月3日 下午10:16:19
	 * @param uid
	 * @return
	 */
	public static ArrayList<NoteInfo> getRecordDatafromServer(String uid){
		ArrayList<NoteInfo> mNoteItemLists = new ArrayList<NoteInfo>();
		NoteInfo noteInfo;
		//获取当前用户
		UserInfo userInfo = UserInfoClient.getUserInfoFromServer(uid);
			AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.TableName.TABLE_NOTE);
			//查询该uid账号下的发帖记录
			query.whereEqualTo("uid", uid);
			query.addAscendingOrder("createdAt");
			try {
			    List<AVObject> avObjects = query.find();
			    for(int i=0;i<avObjects.size();i++){
			        AVObject avoB = avObjects.get(i);
			        noteInfo = new NoteInfo();
			        //获取用户
			        noteInfo.userId= avoB.getString("uid");
			        //获取发表的内容
			        noteInfo.noteContent = avoB.getString("Content");
			        //笔记id
			        noteInfo.noteId = avoB.getString("noteId");
			        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd  hh:ss");
			        Date createDate = avoB.getCreatedAt();
			        //发表的时间
			        String noteTime = "";
			        if(createDate !=null){
			        	noteInfo.noteCreateTime = format.format(createDate);
			        }
			        //获取发表的图片
			        AVFile avFile = avoB.getAVFile("noteImage");
			        if(avFile !=null){
			        	//笔记图片地址
			        	noteInfo.noteImageUrl = avFile.getUrl();
			        }
			        //被赞的次数
			        noteInfo.zan =avoB.getString("zan");
			        //评论数
			        noteInfo.noteComment = getCommentCount(noteInfo.noteId);
			        if (userInfo != null) {
			        	//获取用户头像地址
			        	noteInfo.userImageUrl = userInfo.noteUserImage;
			        	//获取用户昵称
			        	noteInfo.userName = userInfo.userName;
					}
			        
			    	mNoteItemLists.add(noteInfo);
			    	
			    }
			} catch (AVException e) {
			    Log.d("失败", "查询错误: " + e.getMessage());
			}
		return mNoteItemLists;
	}
	/**
	 * 根据noteid查询该笔记评论的条数
	 * Description: TODO
	 * @author: 李伟
	 * @date:2015年4月4日 下午10:50:54
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
