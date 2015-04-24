/**
 * 
 */
package com.scho.entity;

import java.io.Serializable;

/**
 * @author:  liwei
 * @Description:  活动实体类 
 * @date:  2015年4月16日
 */
public class EventsInfo implements Serializable{
	/** 活动id*/
	public String eventsId;
    /** 活动发起人*/
	public String personName="";
	/** 活动开始时间*/
	public String startTime="";
	/** 活动结束时间*/
	public String endTime="";
    /** 活动地点*/	
	public String eventLocation="";
	/** 活动名称*/
	public String eventName="";
	/** 活动内容*/
	public String eventContent="";
}
