/**
 * 
 */
package com.scho.note.widget;

import com.nineoldandroids.view.ViewHelper;
import com.note.ui.activity.R;
import com.scho.note.utils.ScreenUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * @author liwei
 * @Description: 自定义实现一个SlidMenu
 * @date:2015年2月6日 下午1:45:41
 */

public class NoteSlidMenu extends HorizontalScrollView {
    /** 屏幕宽度*/
	private int mScreenWidth; // 屏幕宽度
	/** 菜单栏右边距*/
	private int mMenuRightPadding; // 菜单栏的右边距

	/** 菜单栏的宽度*/
	private int mMenuWidth;
	/** 菜单栏的半边宽度*/
	private int mMenuHalfWidth; // 菜单栏的半边宽度
    /** 标志当前菜单栏是否打开*/
	private boolean isOpen; // 标志当前菜单栏是否打开
    /** 菜单栏是否第一次打开*/
	private boolean isOnce; // 是否第一次打开
    /** 内容页面*/
	private ViewGroup mContent; // 内容页面
	/** 菜单页面*/
	private ViewGroup mMenu; // 菜单页面

	public NoteSlidMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NoteSlidMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ScreenUtils.getScreenWidth(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);

		int n = a.getIndexCount();
		//遍历其所有属性
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			//右边的padding，该值在布局文件中定义
			if (attr == R.styleable.SlidingMenu_rightPadding) {
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));// 默认为10DP
			}
		}
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!isOnce) { // 只允许获得第一次的 测量结果
			LinearLayout ll = (LinearLayout) getChildAt(0);
			//从线性布局中获得自定义的侧滑菜单，注意一定要将该菜单布局放在第一的位置，即将该菜单设为根ViewGroup的第一个子View
			mMenu = (ViewGroup) ll.getChildAt(0); // 从线性布局中获得 Menu布局
			mContent = (ViewGroup) ll.getChildAt(1);
            //菜单栏的宽度=屏幕宽度-与右边距离
			mMenuWidth = mScreenWidth - mMenuRightPadding;
			//一半的菜单栏宽度
			mMenuHalfWidth = mMenuWidth / 2;
			//设置菜单宽度
			mMenu.getLayoutParams().width = mMenuWidth;
			//设置屏幕宽度
			mContent.getLayoutParams().width = mScreenWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0); // 将菜单隐藏
			isOnce = true;
		}
	}

	// 打开菜单
	public void openMenu() {
		if (isOpen) {
			return;
		}
		this.scrollTo(0, 0);
		isOpen = true;
	}

	// 关闭菜单
	public void closeMenu() {
		if (!isOpen) {
			return;
		}
		this.scrollTo(mMenuWidth, 0);
		isOpen = false;
	}

	// 切换菜单开关状态
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	// 监听手势滑动
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			// 获取滑动的横坐标，即手指滑动的距离
			int scrollX = getScrollX(); 
			// 如果滑动大于菜单栏的一半就隐藏
			if (scrollX > mMenuHalfWidth) { 
				this.smoothScrollTo(mMenuWidth, 0); // 隐藏
				isOpen = false;
			} else {
				this.smoothScrollTo(0, 0);// 展开
				isOpen = true;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	// 监听滑动时 菜单视图 和 滑动视图出现相应的放大缩小的特效
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		//得到菜单栏的倒数浮点数，该值的区间范围为、在0.0~1.0之间
		float scale = l * 1.0f / mMenuWidth;
		//左边的缩放尺寸，
		float leftScale = 1 - 0.3f * scale;
		//右边尺寸
		float rightScale = 0.8f + scale * 0.2f;
        
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		//设置透明度，在滑动过程中透明度逐渐加深或变淡，这里设置的是从0.0~1之间变化，将0.0改为0.5，将会在0.5~1.0之间变化
		//注意：如果0.8这个系数需要根据实际情况修改，如果将0.8改为.05会发现当菜单完全展示时期透明度不太高，
		ViewHelper.setAlpha(mMenu, 0.0f + 0.8f * (1 - scale));
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.6f);
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);
	}
}
