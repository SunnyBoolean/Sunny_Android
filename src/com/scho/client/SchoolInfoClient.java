/**
 * 
 */
package com.scho.client;

import java.util.ArrayList;




import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.scho.Constants;
import com.scho.entity.EventsInfo;

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
					info.endTime = obj.getString("act_endTime");
					info.eventLocation = obj.getString("act_location");
					info.eventTheme = obj.getString("act_theme");
					info.eventContent = obj.getString("act_content");
					
					list.add(info);	
					
				}
				}
			} catch (AVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return list;
	}
}
