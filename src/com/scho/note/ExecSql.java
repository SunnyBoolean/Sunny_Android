/**
 * 
 */
package com.scho.note;

import com.scho.note.Constants.TableName;

/**
 * @author liwei
 * @Description:�������ݱ����
 * @date:2015��2��1�� ����12:18:56
 */

public abstract class ExecSql {
    /** ѧ����*/
	public final static String CREATE_STUDENT_TABLE = "CREATE TABLE IF NOT EXISTS ["
            + TableName.TABLE_STUDENT
            + "]("
            + "[OBJECTID] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + "[UUID] VARCHAR2(36),"
            + "[STUNAME] VARCHAR2(20),"
            + "[STUAGE] VARCHAR2(40),"
            + "[STUSEX] VARCHAR2(100)"+")";
}
