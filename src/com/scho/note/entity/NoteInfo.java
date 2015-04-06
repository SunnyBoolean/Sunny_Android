/**
 * 
 */
package com.scho.note.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author liwei
 * @Description: Noteʵ���࣬�û����������ʼǶ��ڹ�����ҳչʾ�������NoteComment��һ�Զ�Ĺ�ϵ
 * @date:2015��2��16�� ����9:07:57
 */

public class NoteInfo implements Serializable{
	/** �ʼ�id��Ψһ��ʶ��Note��User�Ƕ��һ�Ĺ�ϵ*/
	public String noteId;
	/** �ʼ�����id�����ں�User�����*/
    public String userId;
    /** �û�����*/
    public String userName;
    /** �û�ͷ���ַ50*50*/
    public String userImageUrl;
    /** �ʼǱ���*/
    public String noteTitle;
    /** �ʼ�����*/
    public String noteContent;
    /** �ʼ�ͼƬ*/
    public String noteImageUrl;
    /** �ʼǷ���ʱ��*/
    public String noteCreateTime;
    /** �ʼǱ��޵Ĵ����������û���κ��������Ϣ��������¼����*/
    public String noteColcaluations;
    /** �ʼǴ򿨼�¼��*/
    public String noteReportCount;
    /** �ʼǵ�����*/
    public List<NoteCommentInfo> noteComentLists; 
    /** �����۵Ĵ���*/
    public int noteComment = 18;
    /** �Ƿ񹫿�,1��ʾ������0��ʾ˽�л���������*/
    public int isPublic = 1;
    /** ���޵Ĵ���*/
    public String zan;
    /** �������˱ʼ�����������ֶ������ﲻ��������ͼ������������ˣ���Ҫ����������ҳ��ȡ�ʼǵ�ʱ�򣬷���һ���ʼ�����������������������Ͳ��ܼ��ظ���*/
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
