package com.scho.note.utils;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author liwei
 * @Description: SharedPreferences对象的使用，在Setting和保存一些简单值到手机上的工具类 
 * @date:2015年2月3日 上午10:18:28
 */
public class SPUtils
{
	public SPUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 保存在手机里面的文件名
	 */
	public static  String FILE_NAME = "share_data";
	/**
	 * 
	 * Description: 设置保存的文件名，如果不设置默认为share_data
	 * @author: liwei
	 * @date:2015年2月3日 上午10:20:16
	 * @param fileName
	 */
    public static void setFileName(String fileName){
    	FILE_NAME = fileName;
    }


    /**
     * 
     * Description: 保存数据的方法，这里需要知道保存数据的具体类型，然后根据类型调用不同的保存方法。
     * @author: liwei
     * @date:2015年2月3日 上午10:21:19
     * @param context  
     * @param key  数据的key值
     * @param object  数据值
     */
	public static boolean put(Context context, String key, Object object)
	{

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (object instanceof String)
		{
			editor.putString(key, (String) object);
		} else if (object instanceof Integer)
		{
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean)
		{
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float)
		{
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long)
		{
			editor.putLong(key, (Long) object);
		} else
		{
			editor.putString(key, object.toString());
		}

		return SharedPreferencesCompat.apply(editor);
	}


	/**
	 * 
	 * Description: 从手机读取存储的值，得到的是Object值，根据传入的数据类型确认返回的数据值
	 * @author: liwei
	 * @date:2015年2月3日 上午10:37:33
	 * @param context  上下文对象
	 * @param key   保存的数据对应的key值
	 * @param defaultObject  数据类型，根据数据类型获取相应的数据，调用此方法时，必须填写正确
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultObject)
	{
//		FILE_NAME = fileName;
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);

		if (defaultObject instanceof String)
		{
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer)
		{
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean)
		{
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float)
		{
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long)
		{
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}


	/**
	 * 
	 * Description: 一处某个key值已经对于的值
	 * @author: liwei
	 * @date:2015年2月3日 上午10:40:46
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}
	/**
	 * 
	 * Description: 清空所有数据
	 * @author: liwei
	 * @date:2015年2月3日 上午10:41:10
	 * @param context
	 */
	public static void clear(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 
	 * Description: 查询某个key值书否已经存在
	 * @author: liwei
	 * @date:2015年2月3日 上午10:41:33
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.contains(key);
	}

	/**
	 * 
	 * Description: 返回所有的键值对
	 * @author: liwei
	 * @date:2015年2月3日 上午10:41:53
	 * @param context
	 * @return
	 */
	public static Map<String, ?> getAll(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author zhy
	 * 
	 */
	/**
	 * 
	 * @author liwei
	 * @Description:SharedPreferences 有commit（）和apply（）两种方式提交，前者提交后会有返回值是否提交成功
	 *              后者没有返回值，因此无法知道是否提交成功，但是后者的效率比前者高，且后者支持并发，前者不支持。 
	 * @date:2015年2月3日 上午10:50:23
	 */
	private static class SharedPreferencesCompat
	{
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod()
		{
			try
			{
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e)
			{
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
		 */
		public static boolean apply(SharedPreferences.Editor editor)
		{
			try
			{
				if (sApplyMethod != null)
				{
					sApplyMethod.invoke(editor);
					return true;
				}
			} catch (IllegalArgumentException e)
			
			{
				return false;
			} catch (IllegalAccessException e)
			{
				return false;
			} catch (InvocationTargetException e)
			{
				return false;
			}
			return editor.commit();
		}
	}

}