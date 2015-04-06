/**
 * 
 */
package com.scho.note.entity;

import com.scho.note.utils.NoteUtil;

import android.content.ContentValues;

/**
 * @author liwei
 * @Description:TODO
 * @date:2015年2月1日 下午12:29:36
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
	 * 将实体对象转换为ContentValues对象，向数据库插入数据时使用
	 * Description: TODO
	 * @author: liwei
	 * @date:2015年2月1日 下午12:36:15
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
	 * 将ContentValues对象转换为实体类，查询数据库时使用
	 * Description: TODO
	 * @author: liwei
	 * @date:2015年2月1日 下午12:38:23
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
