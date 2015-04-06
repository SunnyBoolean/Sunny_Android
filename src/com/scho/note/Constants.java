/**
 * 
 */
package com.scho.note;

import java.io.File;

import com.scho.note.utils.SDCardUtils;

/**
 * @author liwei
 * @Description:�����࣬�����ݿ��йصĳ�����
 * @date:2015��2��1��
 */

public class Constants {
	/** Ӧ�������ļ������ݿ���·��*/
   public static String NOTE_FILE_DIR = "/";
   /** ��ʼ�������ļ���,���û�д洢���ʹ洢��ϵͳ�洢��*/
   static {
	   if(SDCardUtils.isSDCardEnable()){
		   NOTE_FILE_DIR = SDCardUtils.getSDCardPath()+"/data/note"+File.separator;
	   }else{
		   NOTE_FILE_DIR = SDCardUtils.getRootDirectoryPath()+"/note/data"+File.separator;
	   }
   }
   
   /** ���ݿ�����*/
   public final static String DB_NAME = "note.db";
   /** ���ݿ�汾��*/
   public static int DB_VERSION = 1;
   
   /**
    * ���ݿ����
    * @author liwei
    * @Description:��װ���ݿ������еı��� 
    * @date:2015��2��1�� ����12:16:45
    */
   public static class TableName{
	   /** ѧ����*/
	   public final static String TABLE_STUDENT = "N_STUDENT"; 
	   /** �ʼ�Note��*/
	   public final static String TABLE_NOTE = "N_NOTE";
	   /** �û���*/
	   public final static String TABLE_USER = "_User";
	   /** �ʼ����۱�*/
	   public final static String TABLE_NOTE_NOTECOMMENT = "N_Note_Comment";
   }
   /**
    * 
    * @author liwei
    * @Description:��װActionBar���Զ���ActionBar�ĸ����� 
    * @date:2015��2��12�� ����1:13:09
    */
   public static class ActionBarAttrKey{
	   /** ActionBar�Ŀ��keyֵ����ʵ��ȴ���NoteApplication��map��*/
	   public static int ACTIONBAR_WIDTH = 0;
	   /** ActionBar�ĸߵ�keyֵ����ʵ�߶ȴ���NoteApplication��map��*/
	   public static int ACTIONABR_HEIGHT = 1;
   }
   /**
    * 
    * @author liwei
    * @Description: �û������ģʽ����
    * @date:2015��2��12�� ����1:14:14
    */
   public static class EnterModeCode{
	   /** ����û��ַ��һ��ʹ�ã�ÿ�ε�½�������ֵ�����Ϊfalse�ͻ������������*/
	   public static String SP_KEY_ISFIRST_USE="isFirstIn" ;
   }
   /**
    * 
    * @author ��ΰ
    * @Description:�û�������Ϣ�洢Ϊsp�����key 
    * @date:2015��3��7�� ����3:56:21
    */
   public static class UserInfo{
	   /** �û��Ƿ��½*/
	   public static String IS_LOGIN = "isLogin";
	   /** �û�id��Ψһ��ʶ*/
	   public static String USER_ID = "userid";
	   /** �û�����*/
	   public static String USER_NAME = "username";
	   /** �û�����*/
	   public static String USER_PASSWORD = "userpassword";
	   /** �û�ͷ���ַ*/
	   public static String USER_IMAGE_ICON = "userimage_icon";
	   
	   // ============ ����΢����Ϣ  =============//
	   /** ����΢���û�id*/
	   public final static String SINA_USER_ID = "uid";
	   /** �����û���Ȩ�ɹ�����*/
	   public final static String SINA_ACCESS_TOKEN = "access_token";
	   /** ��Դkey����Ҫ��������΢���ӿ�ʱʹ�õ�����Ӧ��ֵ��appkey*/
	   public final static String SINA_APP_KEY = "source";
   }
   
   public static class Sina_Code{

		/** ����appkey*/
	    public static final String APP_KEY = "1131056026";
	    public static final String REDIRECT_URL = "http://www.sina.com";
	    public static final String APP_SECRET = "3b6c928c61eb22436825c0fc7c1da41d";
	    /** Scope��*/
	    public static final String SCOPE = 
	            "email,direct_messages_read,direct_messages_write,"
	            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
	            + "follow_app_official_microblog," + "invitation_write";
	    
	    /** ��ȡ�û���Ϣ�ӿ�,*/
	    public static final String SINA_USER_INFO_URL= "https://api.weibo.com/2/users/show.json";
   }
}
