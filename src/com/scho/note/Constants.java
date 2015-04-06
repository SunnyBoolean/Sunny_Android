/**
 * 
 */
package com.scho.note;

import java.io.File;

import com.scho.note.utils.SDCardUtils;

/**
 * @author liwei
 * @Description:常量类，与数据库有关的常量等
 * @date:2015年2月1日
 */

public class Constants {
	/** 应用所有文件、数据库存放路径*/
   public static String NOTE_FILE_DIR = "/";
   /** 初始化数据文件夹,如果没有存储卡就存储到系统存储上*/
   static {
	   if(SDCardUtils.isSDCardEnable()){
		   NOTE_FILE_DIR = SDCardUtils.getSDCardPath()+"/data/note"+File.separator;
	   }else{
		   NOTE_FILE_DIR = SDCardUtils.getRootDirectoryPath()+"/note/data"+File.separator;
	   }
   }
   
   /** 数据库名字*/
   public final static String DB_NAME = "note.db";
   /** 数据库版本号*/
   public static int DB_VERSION = 1;
   
   /**
    * 数据库表名
    * @author liwei
    * @Description:封装数据库中所有的表名 
    * @date:2015年2月1日 下午12:16:45
    */
   public static class TableName{
	   /** 学生表*/
	   public final static String TABLE_STUDENT = "N_STUDENT"; 
	   /** 笔记Note表*/
	   public final static String TABLE_NOTE = "N_NOTE";
	   /** 用户表*/
	   public final static String TABLE_USER = "_User";
	   /** 笔记评论表*/
	   public final static String TABLE_NOTE_NOTECOMMENT = "N_Note_Comment";
   }
   /**
    * 
    * @author liwei
    * @Description:封装ActionBar或自定义ActionBar的各属性 
    * @date:2015年2月12日 下午1:13:09
    */
   public static class ActionBarAttrKey{
	   /** ActionBar的宽的key值，真实宽度存在NoteApplication的map中*/
	   public static int ACTIONBAR_WIDTH = 0;
	   /** ActionBar的高的key值，真实高度存在NoteApplication的map中*/
	   public static int ACTIONABR_HEIGHT = 1;
   }
   /**
    * 
    * @author liwei
    * @Description: 用户进入的模式代码
    * @date:2015年2月12日 下午1:14:14
    */
   public static class EnterModeCode{
	   /** 检查用户手否第一次使用，每次登陆都会检查此值，如果为false就会进行引导界面*/
	   public static String SP_KEY_ISFIRST_USE="isFirstIn" ;
   }
   /**
    * 
    * @author 李伟
    * @Description:用户基础信息存储为sp对象的key 
    * @date:2015年3月7日 下午3:56:21
    */
   public static class UserInfo{
	   /** 用户是否登陆*/
	   public static String IS_LOGIN = "isLogin";
	   /** 用户id，唯一标识*/
	   public static String USER_ID = "userid";
	   /** 用户姓名*/
	   public static String USER_NAME = "username";
	   /** 用户密码*/
	   public static String USER_PASSWORD = "userpassword";
	   /** 用户头像地址*/
	   public static String USER_IMAGE_ICON = "userimage_icon";
	   
	   // ============ 新浪微博信息  =============//
	   /** 新浪微博用户id*/
	   public final static String SINA_USER_ID = "uid";
	   /** 新浪用户授权成功代码*/
	   public final static String SINA_ACCESS_TOKEN = "access_token";
	   /** 资源key，主要是在请求微博接口时使用到，对应的值是appkey*/
	   public final static String SINA_APP_KEY = "source";
   }
   
   public static class Sina_Code{

		/** 新浪appkey*/
	    public static final String APP_KEY = "1131056026";
	    public static final String REDIRECT_URL = "http://www.sina.com";
	    public static final String APP_SECRET = "3b6c928c61eb22436825c0fc7c1da41d";
	    /** Scope码*/
	    public static final String SCOPE = 
	            "email,direct_messages_read,direct_messages_write,"
	            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
	            + "follow_app_official_microblog," + "invitation_write";
	    
	    /** 获取用户信息接口,*/
	    public static final String SINA_USER_INFO_URL= "https://api.weibo.com/2/users/show.json";
   }
}
