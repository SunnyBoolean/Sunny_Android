/**
 * 
 */
package com.scho.note.entity;

import android.content.ContentValues;

import com.scho.note.Constants.TableName;
import com.scho.note.database.BaseDatabaseHelper;

/**
 * @author liwei
 * @Description: ѧ�����ݱ������
 * @date:2015��2��1�� ����12:11:16
 */

public class StudentDatabaseHelper extends BaseDatabaseHelper{
	/** StudengDatabaseHelperʵ��*/
    private static StudentDatabaseHelper mInstance;
	/**
	 * @param tableName
	 */
	private StudentDatabaseHelper(String tableName) {
		super(tableName);
	}
    /**
     * ��ȡStudentDatabaseHelperʵ��
     * Description: TODO
     * @author: liwei
     * @date:2015��2��1�� ����12:17:51
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
	 * Description:  ����һ��ѧ����¼
	 * @author: liwei
	 * @date:2015��2��1�� ����12:42:15
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
	 * Description: ��ѯѧ����Ϣ
	 * @author: liwei
	 * @date:2015��2��1�� ����12:43:31
	 * @return
	 */
	public final StudentInfo query(){
		StudentInfo stu = StudentInfo.contentValuesToEntity(get(null, null));
		return stu;
	}
}
