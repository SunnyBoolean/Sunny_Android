/**
 * 
 */
package com.scho.entity;

import java.io.Serializable;

/**
 * @author:  liwei
 * @Description:  备忘录实体 
 * @date:  2015年4月22日
 */
public class MemoInfo implements Serializable{
	/** 备忘录id*/
	public String memoId;
   /** 创建时间*/
	public String createTime;
	/** 备忘录内容*/
	public String content;
	
}
