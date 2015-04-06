/**
 * 
 */
package com.scho.note.entity;

/**
 * @author liwei
 * @Description: ���ڱʼǵ�����ʵ����,�����Note���Ƕ��һ�Ĺ�ϵ����User����һ�Ĺ�ϵ
 * @date:2015��2��16�� ����9:14:36
 */

public class NoteCommentInfo {
    /** ���۵�id��Ψһ��ʶ*/
	public String noCommentId;
	/** �����۵ıʼ�id����id��Note��id�Ƕ�Զ�Ĺ�ϵ*/
	public String noteId;
	/** �����û���id,���ĸ��û����۵ľ���˭��id����id��User id���һ�����*/
	public String userId;
	/** �û��ǳ�,�û���Ϣ����Ӧ�÷��ڴ˴������ǿ��ǵ�������������������Ĵ������Ƿ������ﱣ��*/
	public String userName;
	/** �û�ͷ���ַ*/
	public String userImageUrl;
	/** ��������*/
	public String noContent;
	/** ���۵�ʱ��*/
	public String ntCommentTime;
	public NoteCommentInfo(){}
	public NoteCommentInfo(String userImageUrl,String userName,String commentTime,String commentContent){
		this.userImageUrl = userImageUrl;
		this.userName = userName;
		this.ntCommentTime = commentTime;
		this.noContent = commentContent;
	}
}
