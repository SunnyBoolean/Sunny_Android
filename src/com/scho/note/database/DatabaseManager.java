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
 * @Description:数据库管理类，封装了数据库的打开与关闭，数据的读写操作，其内部类直接继承自SQLiteOpenHelper
 * @date:2015年2月1日
 */

public class DatabaseManager {
	/** 操作帮助类 */
	private DatabaseOpenHelper mDatabaseOpenHelper = null;
	/** 数据库操作类对象 */
	private static DatabaseManager mInstance;

	/**
	 * 构造器私有化
	 * 
	 * @param context
	 */
	private DatabaseManager(Context context) {
		mDatabaseOpenHelper = new DatabaseOpenHelper(context);
	}

	/**
	 * 获取数据库操作类对象
	 * 
	 * @return DatabaseManager实例
	 */
	public static DatabaseManager getInstance() {
		if (null == mInstance) {
			mInstance = new DatabaseManager(NoteApplication.getInstance());
		}
		return mInstance;
	}

	/**
	 * 
	 * Description: 删除本地数据库
	 * 
	 * @author: liwei
	 * @date:2015年2月1日 上午10:34:11
	 * @return: false表示删除失败
	 */
	public static boolean deleteDatabase() {
		try {
			File file = new File(Constants.NOTE_FILE_DIR + Constants.DB_NAME);
			// 如果数据库存在
			if (file.exists()) {
				// 删除数据库
				file.delete();
			}
			// 创建数据库时的日志
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
	 * Description: 取得数据库操作帮助类，创建数据库、打开都靠它了
	 * 
	 * @author: hello
	 * @date:2015年2月1日 上午10:39:22
	 * @return: 返回DatabaseOpenHelper 实例
	 */
	public DatabaseOpenHelper getDatabaseOpenHelper() {
		return mDatabaseOpenHelper;
	}

	/**
	 * 
	 * @author liwei
	 * @Description: 数据库操作基类，所有的数据库打开与读写均由此展开 ，直接继承自SQLiteOpenHelr
	 * @date:2015年2月1日
	 */
	public class DatabaseOpenHelper extends SQLiteOpenHelper {
		/**
		 * @param context
		 * @param name
		 * @param factory
		 * @param version
		 */
		public DatabaseOpenHelper(Context context) {
			// 将数据库创建到指定目录
			super(new ContextWrapper(context), Constants.NOTE_FILE_DIR
					+ Constants.DB_NAME, null, Constants.DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//创建学生表
			try{
				db.execSQL(ExecSql.CREATE_STUDENT_TABLE);
			}catch(SQLException e){
				e.printStackTrace();
			    Toast.makeText(NoteApplication.getInstance(), "创建数据表失败", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// 更新数据库版本的时候在此执行
		}

		/** 为了防止读取冲突，将此方法加锁 */
		@Override
		public synchronized SQLiteDatabase getReadableDatabase() {
			return super.getReadableDatabase();
		}

		/** 为了防止写入冲突，将此方法枷锁 */
		@Override
		public synchronized SQLiteDatabase getWritableDatabase() {
			return super.getWritableDatabase();
		}

	}
}
