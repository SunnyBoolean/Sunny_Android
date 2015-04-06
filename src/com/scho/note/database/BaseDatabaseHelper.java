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
 * @Description:���ݿ�������࣬
 * @date:2015��2��1�� ����10:50:04
 */
public class BaseDatabaseHelper {
	/** ���ݱ��� */
	protected String mTableName = "";

	public BaseDatabaseHelper(String tableName) {
		mTableName = tableName;
	}

	/**
	 * 
	 * Description: ��ȡ��ļ�¼����
	 * 
	 * @author: liwei
	 * @date:2015��2��1�� ����10:53:24
	 * @return: int���ͣ���¼����
	 */
	public int getCount(String tableName) {
		int totalCount = 0;
		// ��ѯ���
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
	 * Description: ����һ������
	 * 
	 * @author: liwei
	 * @date:2015��2��1�� ����11:08:38
	 * @param һ�����ݾ���һ��ContentValues����
	 * @return �������ɹ��ͷ�����β�����ǵڼ������ݣ�����ʧ�ܾͷ���-1
	 */
	public long insert(ContentValues cv) {
		// �����������
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
	 * Description: ����һ�����ݣ�ָ���������
	 * @author: liwei
	 * @date:2015��2��1�� ����11:11:29
	 * @param ����
	 * @param �����ContentValues��������
	 * @return ������ɹ��򷵻ز���ļ�¼����ʧ���򷵻�-1
	 */
	public long insert(String tableName,ContentValues cv){
		// �����������
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
	 * Description: ɾ��һ������
	 * @author: liwei
	 * @date:2015��2��1�� ����11:14:33
	 * @param whereClause: ɾ���������������� name=? and sex = ?
	 * @param whereArgs:ɾ��������������ֵ,��:{"����","��"}
	 * @return ɾ��ʧ�ܼ�����-1
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
	 * Description: ����һ������
	 * @author: liwei
	 * @date:2015��2��1�� ����11:22:18
	 * @param cv ���µ�������
	 * @param whereClause  ����
	 * @param whereArgs ����ֵ
	 * @return
	 */
	public long update(ContentValues cv,String whereClause,String[] whereArgs){
		long rowItem = 0;
		SQLiteDatabase db = null;
		try{
			db = DatabaseManager.getInstance().getDatabaseOpenHelper().getWritableDatabase();
			//��������
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
	 * Description: �ȸ������ݣ�������ʧ�ܣ����������
	 * @author: liwei
	 * @date:2015��2��1�� ����11:25:09
	 * @param cv ���µ�����
	 * @param whereClause ������
	 * @param whereArgs ������ֵ
	 * @return ���ظ��»�����ֵ��-1��ʾ���ºͲ��붼ʧ��
	 */
	public long insertOrUpdate(ContentValues cv,String whereClause,String[] whereArgs){
		long rowItem = update(cv, whereClause, whereArgs);
		return rowItem > 0 ? rowItem : insert(cv);
	}
	/**
	 * 
	 * Description: ʹ�����������������
	 * @author: liwei
	 * @date:2015��2��1�� ����11:37:02
	 * @param ����
	 * @param cvs ���������������
	 * @param �ڲ���ǰ�Ƿ�����ոñ��е����ݣ�true��ʾ��գ�false��ʾ�����
	 * @return ����ɹ��ͷ���true��ʧ�ܷ���false
	 */
	public boolean update(String tableName,List<ContentValues> cvs,boolean isDeleted){
		SQLiteDatabase db = null;
		try{
			db = DatabaseManager.getInstance().getDatabaseOpenHelper().getWritableDatabase();
			//��������
			db.beginTransaction();
			if(isDeleted){
				//�ڲ���ǰ���������
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
	 * escription: ������������
	 * @author: liwei
	 * @date:2015��2��1�� ����11:47:15
	 * @param cvs ���������������
	 * @param isDeleted  ����ǰ�Ƿ����������
	 * @return
	 */
	public boolean update(List<ContentValues> cvs,boolean isDeleted){
		SQLiteDatabase db = null;
		try{
			db = DatabaseManager.getInstance().getDatabaseOpenHelper().getWritableDatabase();
			//��������
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
    * Description: ��ȡ����������ContentValues��Ϸ�б�
    * @author: liwei
    * @date:2015��2��1�� ����12:01:15
    * @param cloumns  ָ����Ҫ����
    * @param whereClause 
    * @param whereArgs
    * @param orderBy
    * @return ����������ContentValues�б�
    */
	public List<ContentValues> get(String[] cloumns,String whereClause,String[] whereArgs,String orderBy){
		List<ContentValues> cvs = new ArrayList<ContentValues>();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try{
			db = DatabaseManager.getInstance().getDatabaseOpenHelper().getWritableDatabase();
			cursor = db.query(mTableName, cloumns, whereClause, whereArgs, null, null, orderBy);
			//��ȡ��ѯ�����ܼ�¼
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
	 * Description: ��ȡ����������ContentValues����
	 * @author: liwei
	 * @date:2015��2��1�� ����12:45:56
	 * @param whereClause  ����
	 * @param whereArgs ����ֵ
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