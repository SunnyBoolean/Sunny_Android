/**
 * 
 */
package com.scho.note.entity;

import java.util.List;

/**
 * @author liwei
 * @Description: �û�ʵ���࣬��װ�û�������Ϣ 
 * @date:2015��2��16�� ����9:18:40
 */

public class UserInfo {
    /** �û�id��Ψһ��ʶ*/
	public String userId;
	/** �û��ǳ�*/
	public String userName;
	/** �û��ֻ���*/
	public String userTelPhone;
	/** �û�����*/
	public int age;
	/** �û�ע��ʱ��*/
	public String regTime;
	/** �û��ҵ���ҫ��Ŀ*/
	public String honerCount;
	/** �û������ıʼǼ�¼*/
	public List<NoteInfo> noteRecordLists;
	/** 50*50���û�Сͼ������ʾ�ʼǵ�ʱ����ʾ�û�ͷ��*/
	public String noteUserImage = "";
	/** �û����е����ۼ�¼*/
	public List<NoteCommentInfo> noteCommentRecordLists;
	
	// ======== ΢�����  =======
	/** ΢���˺�id*/
	public String sinaUserId;
	/** ΢���ǳ�*/
	public String sinaUserName;
	/** ΢���˻�ͼƬ��ַ*/
	public String sinaUserImageUrl;
}
