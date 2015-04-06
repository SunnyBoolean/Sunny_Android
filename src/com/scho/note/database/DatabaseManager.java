/**
 * 
 */
package com.scho.note.database;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.scho.note.Constants;
import com.scho.note.ExecSql;
import com.scho.note.system.NoteApplication;

/**
 * @author liwei
 * @Description:���ݿ�����࣬��װ�����ݿ�Ĵ���رգ����ݵĶ�д���������ڲ���ֱ�Ӽ̳���SQLiteOpenHelper
 * @date:2015��2��1��
 */

public class DatabaseManager {
	/** ���������� */
	private DatabaseOpenHelper mDatabaseOpenHelper = null;
	/** ���ݿ��������� */
	private static DatabaseManager mInstance;

	/**
	 * ������˽�л�
	 * 
	 * @param context
	 */
	private DatabaseManager(Context context) {
		mDatabaseOpenHelper = new DatabaseOpenHelper(context);
	}

	/**
	 * ��ȡ���ݿ���������
	 * 
	 * @return DatabaseManagerʵ��
	 */
	public static DatabaseManager getInstance() {
		if (null == mInstance) {
			mInstance = new DatabaseManager(NoteApplication.getInstance());
		}
		return mInstance;
	}

	/**
	 * 
	 * Description: ɾ���������ݿ�
	 * 
	 * @author: liwei
	 * @date:2015��2��1�� ����10:34:11
	 * @return: false��ʾɾ��ʧ��
	 */
	public static boolean deleteDatabase() {
		try {
			File file = new File(Constants.NOTE_FILE_DIR + Constants.DB_NAME);
			// ������ݿ����
			if (file.exists()) {
				// ɾ�����ݿ�
				file.delete();
			}
			// �������ݿ�ʱ����־
			file = new File(Constants.NOTE_FILE_DIR + Constants.DB_NAME
					+ "-journal");
			if (file.exists()) {
				file.delete();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * Description: ȡ�����ݿ���������࣬�������ݿ⡢�򿪶�������
	 * 
	 * @author: hello
	 * @date:2015��2��1�� ����10:39:22
	 * @return: ����DatabaseOpenHelper ʵ��
	 */
	public DatabaseOpenHelper getDatabaseOpenHelper() {
		return mDatabaseOpenHelper;
	}

	/**
	 * 
	 * @author liwei
	 * @Description: ���ݿ�������࣬���е����ݿ�����д���ɴ�չ�� ��ֱ�Ӽ̳���SQLiteOpenHelr
	 * @date:2015��2��1��
	 */
	public class DatabaseOpenHelper extends SQLiteOpenHelper {
		/**
		 * @param context
		 * @param name
		 * @param factory
		 * @param version
		 */
		public DatabaseOpenHelper(Context context) {
			// �����ݿⴴ����ָ��Ŀ¼
			super(new ContextWrapper(context), Constants.NOTE_FILE_DIR
					+ Constants.DB_NAME, null, Constants.DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//����ѧ����
			try{
				db.execSQL(ExecSql.CREATE_STUDENT_TABLE);
			}catch(SQLException e){
				e.printStackTrace();
			    Toast.makeText(NoteApplication.getInstance(), "�������ݱ�ʧ��", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// �������ݿ�汾��ʱ���ڴ�ִ��
		}

		/** Ϊ�˷�ֹ��ȡ��ͻ�����˷������� */
		@Override
		public synchronized SQLiteDatabase getReadableDatabase() {
			return super.getReadableDatabase();
		}

		/** Ϊ�˷�ֹд���ͻ�����˷������� */
		@Override
		public synchronized SQLiteDatabase getWritableDatabase() {
			return super.getWritableDatabase();
		}

	}
}
