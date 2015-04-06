/**
 * 
 */
package com.scho.note.entity;

import java.util.List;

/**
 * @author liwei
 * @Description: 用户实体类，封装用户基本信息 
 * @date:2015年2月16日 下午9:18:40
 */

public class UserInfo {
    /** 用户id，唯一标识*/
	public String userId;
	/** 用户昵称*/
	public String userName;
	/** 用户手机号*/
	public String userTelPhone;
	/** 用户年龄*/
	public int age;
	/** 用户注册时间*/
	public String regTime;
	/** 用户我的荣耀数目*/
	public String honerCount;
	/** 用户发过的笔记记录*/
	public List<NoteInfo> noteRecordLists;
	/** 50*50的用户小图像，在显示笔记的时候显示用户头像*/
	public String noteUserImage = "";
	/** 用户所有的评论记录*/
	public List<NoteCommentInfo> noteCommentRecordLists;
	
	// ======== 微博相关  =======
	/** 微博账号id*/
	public String sinaUserId;
	/** 微博昵称*/
	public String sinaUserName;
	/** 微博账户图片地址*/
	public String sinaUserImageUrl;
}
