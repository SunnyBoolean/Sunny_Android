/**
 * 
 */
package com.scho.entity;

import java.io.Serializable;

/**
 * @author:  liwei
 * @Description:  �ʵ���� 
 * @date:  2015��4��16��
 */
public class EventsInfo implements Serializable{
	/** �id*/
	public String eventsId;
    /** �������*/
	public String personName="";
	/** ���ʼʱ��*/
	public String startTime="";
	/** �����ʱ��*/
	public String endTime="";
    /** ��ص�*/	
	public String eventLocation="";
	/** �����*/
	public String eventName="";
	/** �����*/
	public String eventContent="";
}
