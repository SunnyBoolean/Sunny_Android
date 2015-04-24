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
 * @Description:  从服务器获取学校信息类，包括获取风景图片，学校简介等
 * @date:  2015年4月16日
 */
public class SchoolInfoClient {
    /** 风景图片url*/
	private static ArrayList<String> schoolImage = new ArrayList<String>();
	
	public static ArrayList<String> getSchoolImage(){
		//查询文件表
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
	 * 获取活动信息
	 * @return
	 */
	public static ArrayList<EventsInfo> getEventsClient(){
		ArrayList<EventsInfo> list = new ArrayList<EventsInfo>();
		AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.SCHO_EVENTS);
//		query.whereEqualTo("noteId", noteId);
		//时间降序排序，
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
	 * 上传新建活动
	 * @param info  活动内容
	 * @return  true表示上传成功  false失败
	 */
	public static HttpResultInfo uploadNewEvent(EventsInfo info,final Context context){
		final HttpResultInfo result = new HttpResultInfo();
		AVObject obj = new AVObject(Constants.SCHO_EVENTS);
		//活动id，唯一标识
		obj.put("act_id", NoteUtil.getId());
		//活动发起人
		obj.put("act_person", info.personName);
		//活动开始时间
		obj.put("act_starttime", info.startTime);
		//活动结束时间
		obj.put("act_endtime", info.endTime);
		//活动名称
		obj.put("act_name", info.eventName);
		//活动内容
		obj.put("act_content", info.eventContent);
		//活动地点
		obj.put("act_location", info.eventLocation);
		
		obj.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(AVException arg0) {
				if(arg0==null){
					result.success = true;
					result.resultInfo = "活动新建上传成功！";
					Toast.makeText(context, "上传成功！", Toast.LENGTH_LONG).show();
				}else{
					result.success = false;
					result.resultInfo = "上传失败！请检查网络！";
					Toast.makeText(context, "上传失败！", Toast.LENGTH_LONG).show();
				}
			}
		});
		return result;
	}
	/**
	 * 获取备忘录信息
	 * @return
	 */
	public static ArrayList<MemoInfo> getMemoClient(){
		ArrayList<MemoInfo> list = new ArrayList<MemoInfo>();
		AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.SCHO_PERSONLA_MEMO);
//		query.whereEqualTo("noteId", noteId);
		//时间降序排序，
		query.orderByDescending("createdAt");
		List<AVObject> objects = null;
			try {
				objects = query.find();
				if(objects !=null){
					MemoInfo info;
				for(int i=0;i<objects.size();i++){
					AVObject obj = objects.get(i);
					info = new MemoInfo();
					//获取内容
					info.content = obj.getString("memo_content");
					//备忘录id
					info.memoId = obj.getObjectId();
					//时间
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
	 * 上传新建备忘录
	 * @param info  新建的内容
	 * @param context
	 * @return
	 */
	public static HttpResultInfo uploadNewMemo(MemoInfo info,final Context context){
		final HttpResultInfo result = new HttpResultInfo();
		AVObject obj = new AVObject(Constants.SCHO_PERSONLA_MEMO);
		//备忘录id，唯一标识
		obj.put("memo_id", NoteUtil.getId());
		//备忘录内容
		obj.put("memo_content", info.content);
		obj.saveInBackground(new SaveCallback() {
			@Override
			public void done(AVException arg0) {
				if(arg0==null){
					result.success = true;
					result.resultInfo = "备忘录新建上传成功！";
					Toast.makeText(context, "上传成功！", Toast.LENGTH_LONG).show();
				}else{
					result.success = false;
					result.resultInfo = "上传失败！请检查网络！";
					Toast.makeText(context, "上传失败！", Toast.LENGTH_LONG).show();
				}
			}
		});
		return result;
	}
	/**
	 * 获取指定表的总记录数
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
	 * 删除表记录，主要包括活动记录、备忘录记录
	 * @param itemId  记录id
	 * @param tableName  表名字
	 * @return  true表示删除成功
	 */
	public static boolean deleteEvents(String itemId,String tableName){
		//查询指定对象
		AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
		AVObject obj;
		try {
			//查找对象
			obj = query.get(itemId);
			if(obj !=null){
				//然后删除之
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
