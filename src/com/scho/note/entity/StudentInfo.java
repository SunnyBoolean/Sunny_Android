/**
 * 
 */
package com.scho.note.entity;

import com.scho.note.utils.NoteUtil;

import android.content.ContentValues;

/**
 * @author liwei
 * @Description:TODO
 * @date:2015��2��1�� ����12:29:36
 */

public class StudentInfo {
	public String name;
	public String sex;
	public String age;
	public StudentInfo(){}
    public StudentInfo(String name,String sex,String age){
    	this.name = name;
    	this.sex = sex;
    	this.age = age;
    }
	/**
	 * ��ʵ�����ת��ΪContentValues���������ݿ��������ʱʹ��
	 * Description: TODO
	 * @author: liwei
	 * @date:2015��2��1�� ����12:36:15
	 * @param stu
	 * @return
	 */
	public static ContentValues entityToContentValues(StudentInfo stu) {
		ContentValues cv = new ContentValues();
		cv.put("STUNAME", stu.name);
		cv.put("STUSEX", stu.sex);
		cv.put("STUAGE", stu.age);
		cv.put("UUID", NoteUtil.getId());
		return cv;
	}
	/**
	 * ��ContentValues����ת��Ϊʵ���࣬��ѯ���ݿ�ʱʹ��
	 * Description: TODO
	 * @author: liwei
	 * @date:2015��2��1�� ����12:38:23
	 * @param cv
	 * @return
	 */
	public static StudentInfo contentValuesToEntity(ContentValues cv){
		StudentInfo stu = new StudentInfo();
		stu.name = cv.getAsString("STUNAME");
		stu.age = cv.getAsString("STUAGE");
		stu.sex = cv.getAsString("STUSEX");
		return stu;
	}
}
