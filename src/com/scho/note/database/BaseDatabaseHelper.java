/**
 * 
 */
package com.scho.note.database;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author liwei
 * @Description:数据库操作基类，
 * @date:2015年2月1日 上午10:50:04
 */
public class BaseDatabaseHelper {
	/** 数据表名 */
	protected String mTableName = "";

	public BaseDatabaseHelper(String tableName) {
		mTableName = tableName;
	}

	/**
	 * 
	 * Description: 获取表的记录条数
	 * 
	 * @author: liwei
	 * @date:2015年2月1日 上午10:53:24
	 * @return: int类型，记录总数
	 */
	public int getCount(String tableName) {
		int totalCount = 0;
		// 查询语句
		String searchSql = "SELECT COUNT(*) FROM " + tableName;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = DatabaseManager.getInstance().getDatabaseOpenHelper()
					.getWritableDatabase();
			cursor = db.rawQuery(searchSql, null);
			if (cursor.moveToNext()) {
				totalCount = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
			if (null != db && db.isOpen()) {
				db.close();
			}
		}
		return totalCount;
	}

	/**
	 * 
	 * Description: 插入一条数据
	 * 
	 * @author: liwei
	 * @date:2015年2月1日 上午11:08:38
	 * @param 一天数据就是一个ContentValues对象
	 * @return 如果插入成功就返回这次插入的是第几天数据，插入失败就返回-1
	 */
	public long insert(ContentValues cv) {
		// 插入的条数，
		long rowItem = -1;
		SQLiteDatabase db = null;
		try {
			db = DatabaseManager.getInstance().getDatabaseOpenHelper()
					.getWritableDatabase();
			rowItem = db.insertOrThrow(mTableName, null, cv);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != db && db.isOpen()) {
				db.close();
			}
		}
		return rowItem;
	}
	/**
	 * 
	 * Description: 插入一条数据，指定主句表名
	 * @author: liwei
	 * @date:2015年2月1日 上午11:11:29
	 * @param 表名
	 * @param 插入的ContentValues对象数据
	 * @return 若插入成功则返回插入的记录数，失败则返回-1
	 */
	public long insert(String tableName,ContentValues cv){
		// 插入的条数，
				long rowItem = -1;
				SQLiteDatabase db = null;
				try {
					db = DatabaseManager.getInstance().getDatabaseOpenHelper()
							.getWritableDatabase();
					rowItem = db.insertOrThrow(mTableName, null, cv);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (null != db && db.isOpen()) {
						db.close();
					}
				}
				return rowItem;
	}
	/**
	 * 
	 * Description: 删除一条数据
	 * @author: liwei
	 * @date:2015年2月1日 上午11:14:33
	 * @param whereClause: 删除语句的子条件，如 name=? and sex = ?
	 * @param whereArgs:删除语句的子条件的值,如:{"张三","男"}
	 * @return 删除失败即返回-1
	 */
	public long delete(String whereClause,String[] whereArgs){
		long rowItem = -1;
		SQLiteDatabase db = null;
		try{
			db = DatabaseManager.getInstance().getDatabaseOpenHelper().getWritableDatabase();
			rowItem = db.delete(mTableName, whereClause, whereArgs);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if (null != db && db.isOpen()) {
				db.close();
			}
		}
		return rowItem;
	}
	/**
	 * 
	 * Description: 更新一条数据
	 * @author: liwei
	 * @date:2015年2月1日 上午11:22:18
	 * @param cv 更新的新数据
	 * @param whereClause  条件
	 * @param whereArgs 条件值
	 * @return
	 */
	public long update(ContentValues cv,String whereClause,String[] whereArgs){
		long rowItem = 0;
		SQLiteDatabase db = null;
		try{
			db = DatabaseManager.getInstance().getDatabaseOpenHelper().getWritableDatabase();
			//开启事物
			db.beginTransaction();
			rowItem = db.update(mTableName, cv, whereClause, whereArgs);
			db.setTransactionSuccessful();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != db && db.isOpen()){
				db.endTransaction();
				db.close();
			}
		}
		return rowItem;
	}
	/**
	 * 
	 * Description: 先更新数据，若更新失败，则插入数据
	 * @author: liwei
	 * @date:2015年2月1日 上午11:25:09
	 * @param cv 更新的数据
	 * @param whereClause 子条件
	 * @param whereArgs 子条件值
	 * @return 返回更新或插入的值，-1表示更新和插入都失败
	 */
	public long insertOrUpdate(ContentValues cv,String whereClause,String[] whereArgs){
		long rowItem = update(cv, whereClause, whereArgs);
		return rowItem > 0 ? rowItem : insert(cv);
	}
	/**
	 * 
	 * Description: 使用事物，批量插入数据
	 * @author: liwei
	 * @date:2015年2月1日 上午11:37:02
	 * @param 表名
	 * @param cvs 待插入的批量数据
	 * @param 在插入前是否先清空该表中的数据，true表示清空，false表示不清空
	 * @return 插入成功就返回true，失败返回false
	 */
	public boolean update(String tableName,List<ContentValues> cvs,boolean isDeleted){
		SQLiteDatabase db = null;
		try{
			db = DatabaseManager.getInstance().getDatabaseOpenHelper().getWritableDatabase();
			//开启事物
			db.beginTransaction();
			if(isDeleted){
				//在插入前先清空数据
				db.delete(tableName, null, null);
			}
			for(ContentValues cv : cvs){
				db.insert(tableName, null, cv);
			}
			db.setTransactionSuccessful();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(null != db && db.isOpen()){
				db.endTransaction();
				db.close();
			}
		}
	}
	/**
	 * 
	 * escription: 批量更新数据
	 * @author: liwei
	 * @date:2015年2月1日 上午11:47:15
	 * @param cvs 待批量插入的数据
	 * @param isDeleted  插入前是否先情况数据
	 * @return
	 */
	public boolean update(List<ContentValues> cvs,boolean isDeleted){
		SQLiteDatabase db = null;
		try{
			db = DatabaseManager.getInstance().getDatabaseOpenHelper().getWritableDatabase();
			//开启事物
			db.beginTransaction();
			if(isDeleted){
				db.delete(mTableName, null, null);
			}
			for(ContentValues cv : cvs){
				db.insert(mTableName, null, null);
			}
			db.setTransactionSuccessful();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			db.endTransaction();
			db.close();
		}
		
	}
   /**
    * 
    * Description: 获取符合条件的ContentValues对戏列表
    * @author: liwei
    * @date:2015年2月1日 下午12:01:15
    * @param cloumns  指定需要的列
    * @param whereClause 
    * @param whereArgs
    * @param orderBy
    * @return 符合条件的ContentValues列表
    */
	public List<ContentValues> get(String[] cloumns,String whereClause,String[] whereArgs,String orderBy){
		List<ContentValues> cvs = new ArrayList<ContentValues>();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try{
			db = DatabaseManager.getInstance().getDatabaseOpenHelper().getWritableDatabase();
			cursor = db.query(mTableName, cloumns, whereClause, whereArgs, null, null, orderBy);
			//获取查询到的总记录
		    int columnCount = cursor.getColumnCount();
		    while(cursor.moveToNext()){
		    	ContentValues cv = new ContentValues();
		    	for(int i = 0;i<columnCount;i++){
		    		cv.put(cursor.getColumnName(i), cursor.getString(i));
		    	}
		    	cvs.add(cv);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != cursor && !cursor.isClosed()){
				cursor.close();
			}
			if(null != db && db.isOpen()){
				db.close();
			}
		}
		return cvs;
	}
	/**
	 * 
	 * Description: 获取符合条件的ContentValues对象
	 * @author: liwei
	 * @date:2015年2月1日 下午12:45:56
	 * @param whereClause  条件
	 * @param whereArgs 条件值
	 * @return
	 */
	 public ContentValues get(String whereClause, String[] whereArgs) {
	        ContentValues cv = new ContentValues();
	        Cursor cursor = null;
	        SQLiteDatabase db = null;
	        try {
	            db = DatabaseManager.getInstance().getDatabaseOpenHelper().getReadableDatabase();
	            cursor = db.query(mTableName, null, whereClause, whereArgs, null, null, null);
	            int columnCount = cursor.getColumnCount();
	            while (cursor.moveToNext()) {
	                for (int i = 0; i < columnCount; i++) {
	                    cv.put(cursor.getColumnName(i), cursor.getString(i));
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (null != cursor && !cursor.isClosed()) {
	                cursor.close();
	            }
	            if (null != db && db.isOpen()) {
	                db.close();
	            }
	        }
	        return cv;
	    }

}