/**
 * 
 */
package com.scho.note.entity;

import android.content.ContentValues;

import com.scho.note.Constants.TableName;
import com.scho.note.database.BaseDatabaseHelper;

/**
 * @author liwei
 * @Description: 学生数据表帮助类
 * @date:2015年2月1日 下午12:11:16
 */

public class StudentDatabaseHelper extends BaseDatabaseHelper{
	/** StudengDatabaseHelper实例*/
    private static StudentDatabaseHelper mInstance;
	/**
	 * @param tableName
	 */
	private StudentDatabaseHelper(String tableName) {
		super(tableName);
	}
    /**
     * 获取StudentDatabaseHelper实例
     * Description: TODO
     * @author: liwei
     * @date:2015年2月1日 下午12:17:51
     * @return
     */
	public static StudentDatabaseHelper getStudentHelperInstance(){
		if(null == mInstance){
			mInstance = new StudentDatabaseHelper(TableName.TABLE_STUDENT);
		}
		return mInstance;
	}
	/**
	 *
	 * Description:  插入一条学生记录
	 * @author: liwei
	 * @date:2015年2月1日 下午12:42:15
	 * @param stu
	 * @return
	 */
	public final long insert(StudentInfo stu){
		long rowItem = -1;
		ContentValues cv = StudentInfo.entityToContentValues(stu);
		rowItem = insert(cv);
		return rowItem;
	}
	/**
	 * 
	 * Description: 查询学生信息
	 * @author: liwei
	 * @date:2015年2月1日 下午12:43:31
	 * @return
	 */
	public final StudentInfo query(){
		StudentInfo stu = StudentInfo.contentValuesToEntity(get(null, null));
		return stu;
	}
}
