/**
 * 
 */
package com.scho.note.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author liwei
 * @Description: Note实体类，用户发表的主题笔记都在公众首页展示，此类和NoteComment是一对多的关系
 * @date:2015年2月16日 下午9:07:57
 */

public class NoteInfo implements Serializable{
	/** 笔记id，唯一标识，Note和User是多对一的关系*/
	public String noteId;
	/** 笔记作者id，用于和User相关联*/
    public String userId;
    /** 用户名字*/
    public String userName;
    /** 用户头像地址50*50*/
    public String userImageUrl;
    /** 笔记标题*/
    public String noteTitle;
    /** 笔记内容*/
    public String noteContent;
    /** 笔记图片*/
    public String noteImageUrl;
    /** 笔记发表时间*/
    public String noteCreateTime;
    /** 笔记被赞的次数，这个赞没有任何相关联信息，仅仅记录数字*/
    public String noteColcaluations;
    /** 笔记打卡记录数*/
    public String noteReportCount;
    /** 笔记的评论*/
    public List<NoteCommentInfo> noteComentLists; 
    /** 被评论的次数*/
    public int noteComment = 18;
    /** 是否公开,1表示公开，0表示私有化，不公开*/
    public int isPublic = 1;
    /** 被赞的次数*/
    public String zan;
    /** 服务器端笔记总数，这个字段在这里不合理，但是图方便放在这里了，主要是用于在首页获取笔记的时候，返回一个笔记数量总数，大于这个总数就不能加载更多*/
    public int noteCount;
    public NoteInfo(){}
    public NoteInfo(String noteId,String userId,String userName,String userImageUrl,String noteContent,String noteImageUrl,String noteCreateTime,int noteComment,String noteCu){
    	this.noteId = noteId;
    	this.userName = userName;
    	this.userImageUrl = userImageUrl;
    	this.noteContent = noteContent;
    	this.noteImageUrl = noteImageUrl;
    	this.noteCreateTime = noteCreateTime;
    	this.noteComment = noteComment;
    	this.userId = userId;
    	this.noteColcaluations =noteCu; 
    }
}
