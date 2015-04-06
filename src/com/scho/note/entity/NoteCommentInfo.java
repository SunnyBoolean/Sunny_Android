/**
 * 
 */
package com.scho.note.entity;

/**
 * @author liwei
 * @Description: 公众笔记的评论实体类,该类和Note类是多对一的关系，和User类多对一的关系
 * @date:2015年2月16日 下午9:14:36
 */

public class NoteCommentInfo {
    /** 评论的id，唯一标识*/
	public String noCommentId;
	/** 所评论的笔记id，此id和Note的id是多对多的关系*/
	public String noteId;
	/** 评论用户的id,是哪个用户评论的就是谁的id，此id和User id多对一相关联*/
	public String userId;
	/** 用户昵称,用户信息本不应该放在此处，但是考虑到尽量减少请求服务器的次数还是放在这里保存*/
	public String userName;
	/** 用户头像地址*/
	public String userImageUrl;
	/** 评论内容*/
	public String noContent;
	/** 评论的时间*/
	public String ntCommentTime;
	public NoteCommentInfo(){}
	public NoteCommentInfo(String userImageUrl,String userName,String commentTime,String commentContent){
		this.userImageUrl = userImageUrl;
		this.userName = userName;
		this.ntCommentTime = commentTime;
		this.noContent = commentContent;
	}
}
