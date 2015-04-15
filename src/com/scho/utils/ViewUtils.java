package com.scho.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 
 * @author liwei
 * @Description: View的工具类
 * @date:2015年2月3日 下午1:54:30
 */
public class ViewUtils {

	private ViewUtils() {
		throw new AssertionError();
	}

	/**
	 * 
	 * Description: 获取ListView的高度
	 * 
	 * @author: liwei
	 * @date:2015年2月3日 下午1:55:10
	 * @param view
	 * @return
	 */
	public static int getListViewHeightBasedOnChildren(ListView view) {
		int height = getAbsListViewHeightBasedOnChildren(view);
		ListAdapter adapter;
		int adapterCount;
		if (view != null && (adapter = view.getAdapter()) != null
				&& (adapterCount = adapter.getCount()) > 0) {
			height += view.getDividerHeight() * (adapterCount - 1);
		}
		return height;
	}

	private static final String CLASS_NAME_GRID_VIEW = "android.widget.GridView";
	private static final String FIELD_NAME_VERTICAL_SPACING = "mVerticalSpacing";

	/**
	 * 
	 * Description: 获取GridView的间距
	 * @author: liwei
	 * @date:2015年2月3日 下午1:57:04
	 * @param view
	 * @return
	 */
	public static int getGridViewVerticalSpacing(GridView view) {
		// get mVerticalSpacing by android.widget.GridView
		Class<?> demo = null;
		int verticalSpacing = 0;
		try {
			demo = Class.forName(CLASS_NAME_GRID_VIEW);
			Field field = demo.getDeclaredField(FIELD_NAME_VERTICAL_SPACING);
			field.setAccessible(true);
			verticalSpacing = (Integer) field.get(view);
			return verticalSpacing;
		} catch (Exception e) {
			/**
			 * accept all exception, include ClassNotFoundException,
			 * NoSuchFieldException, InstantiationException,
			 * IllegalArgumentException, IllegalAccessException,
			 * NullPointException
			 */
			e.printStackTrace();
		}
		return verticalSpacing;
	}

	/**
	 * get AbsListView height according to every children
	 * 
	 * @param view
	 * @return
	 */
	public static int getAbsListViewHeightBasedOnChildren(AbsListView view) {
		ListAdapter adapter;
		if (view == null || (adapter = view.getAdapter()) == null) {
			return 0;
		}

		int height = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i, null, view);
			if (item instanceof ViewGroup) {
				item.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			item.measure(0, 0);
			height += item.getMeasuredHeight();
		}
		height += view.getPaddingTop() + view.getPaddingBottom();
		return height;
	}

	/**
	 * 
	 * Description: 设置修改一个视图的高
	 * @author: liwei
	 * @date:2015年2月3日 下午1:58:10
	 * @param view
	 * @param height
	 */
	public static void setViewHeight(View view, int height) {
		if (view == null) {
			return;
		}

		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height = height;
	}

	/**
	 * set ListView height which is calculated by
	 * {@link # getListViewHeightBasedOnChildren(ListView)}
	 * 
	 * @param view
	 * @return
	 */
	public static void setListViewHeightBasedOnChildren(ListView view) {
		setViewHeight(view, getListViewHeightBasedOnChildren(view));
	}

	/**
	 * set AbsListView height which is calculated by
	 * {@link # getAbsListViewHeightBasedOnChildren(AbsListView)}
	 * 
	 * @param view
	 * @return
	 */
	public static void setAbsListViewHeightBasedOnChildren(AbsListView view) {
		setViewHeight(view, getAbsListViewHeightBasedOnChildren(view));
	}

	/**
	 * set SearchView OnClickListener
	 * 
	 * @param v
	 * @param listener
	 */
	public static void setSearchViewOnClickListener(View v,
			OnClickListener listener) {
		if (v instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) v;
			int count = group.getChildCount();
			for (int i = 0; i < count; i++) {
				View child = group.getChildAt(i);
				if (child instanceof LinearLayout
						|| child instanceof RelativeLayout) {
					setSearchViewOnClickListener(child, listener);
				}

				if (child instanceof TextView) {
					TextView text = (TextView) child;
					text.setFocusable(false);
				}
				child.setOnClickListener(listener);
			}
		}
	}

	/**
	 * get descended views from parent.
	 * 
	 * @param parent
	 * @param filter
	 *            Type of views which will be returned.
	 * @param includeSubClass
	 *            Whether returned list will include views which are subclass of
	 *            filter or not.
	 * @return
	 */
	public static <T extends View> List<T> getDescendants(ViewGroup parent,
			Class<T> filter, boolean includeSubClass) {
		List<T> descendedViewList = new ArrayList<T>();
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = parent.getChildAt(i);
			Class<? extends View> childsClass = child.getClass();
			if ((includeSubClass && filter.isAssignableFrom(childsClass))
					|| (!includeSubClass && childsClass == filter)) {
				descendedViewList.add(filter.cast(child));
			}
			if (child instanceof ViewGroup) {
				descendedViewList.addAll(getDescendants((ViewGroup) child,
						filter, includeSubClass));
			}
		}
		return descendedViewList;
	}
	/**
 	 * 
 	 * Description: 修改所有界面字体的大小
 	 * @author: liwei
 	 * @date:2015年2月12日 下午1:55:32
 	 * @param root 父视图
 	 * @param size 大小
 	 * @param act Activity对象
 	 */
  	public static void changeTextSize(ViewGroup root,int size, Activity act) {  
         for (int i = 0; i < root.getChildCount(); i++) {  
             View v = root.getChildAt(i);  
             if (v instanceof TextView) {  
                ((TextView) v).setTextSize(size);
             } else if (v instanceof Button) {  
            	((Button) v).setTextSize(size);
             } else if (v instanceof EditText) {  
            	((EditText) v).setTextSize(size);  
             } else if (v instanceof ViewGroup) {  
                changeTextSize((ViewGroup) v,size,act);  
             }  
         }  
      }
}
