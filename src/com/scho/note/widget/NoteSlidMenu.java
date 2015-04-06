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
 * @Description: �Զ���ʵ��һ��SlidMenu
 * @date:2015��2��6�� ����1:45:41
 */

public class NoteSlidMenu extends HorizontalScrollView {
    /** ��Ļ���*/
	private int mScreenWidth; // ��Ļ���
	/** �˵����ұ߾�*/
	private int mMenuRightPadding; // �˵������ұ߾�

	/** �˵����Ŀ��*/
	private int mMenuWidth;
	/** �˵����İ�߿��*/
	private int mMenuHalfWidth; // �˵����İ�߿��
    /** ��־��ǰ�˵����Ƿ��*/
	private boolean isOpen; // ��־��ǰ�˵����Ƿ��
    /** �˵����Ƿ��һ�δ�*/
	private boolean isOnce; // �Ƿ��һ�δ�
    /** ����ҳ��*/
	private ViewGroup mContent; // ����ҳ��
	/** �˵�ҳ��*/
	private ViewGroup mMenu; // �˵�ҳ��

	public NoteSlidMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NoteSlidMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ScreenUtils.getScreenWidth(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);

		int n = a.getIndexCount();
		//��������������
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			//�ұߵ�padding����ֵ�ڲ����ļ��ж���
			if (attr == R.styleable.SlidingMenu_rightPadding) {
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));// Ĭ��Ϊ10DP
			}
		}
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!isOnce) { // ֻ�����õ�һ�ε� �������
			LinearLayout ll = (LinearLayout) getChildAt(0);
			//�����Բ����л���Զ���Ĳ໬�˵���ע��һ��Ҫ���ò˵����ַ��ڵ�һ��λ�ã������ò˵���Ϊ��ViewGroup�ĵ�һ����View
			mMenu = (ViewGroup) ll.getChildAt(0); // �����Բ����л�� Menu����
			mContent = (ViewGroup) ll.getChildAt(1);
            //�˵����Ŀ��=��Ļ���-���ұ߾���
			mMenuWidth = mScreenWidth - mMenuRightPadding;
			//һ��Ĳ˵������
			mMenuHalfWidth = mMenuWidth / 2;
			//���ò˵����
			mMenu.getLayoutParams().width = mMenuWidth;
			//������Ļ���
			mContent.getLayoutParams().width = mScreenWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0); // ���˵�����
			isOnce = true;
		}
	}

	// �򿪲˵�
	public void openMenu() {
		if (isOpen) {
			return;
		}
		this.scrollTo(0, 0);
		isOpen = true;
	}

	// �رղ˵�
	public void closeMenu() {
		if (!isOpen) {
			return;
		}
		this.scrollTo(mMenuWidth, 0);
		isOpen = false;
	}

	// �л��˵�����״̬
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	// �������ƻ���
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			// ��ȡ�����ĺ����꣬����ָ�����ľ���
			int scrollX = getScrollX(); 
			// ����������ڲ˵�����һ�������
			if (scrollX > mMenuHalfWidth) { 
				this.smoothScrollTo(mMenuWidth, 0); // ����
				isOpen = false;
			} else {
				this.smoothScrollTo(0, 0);// չ��
				isOpen = true;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	// ��������ʱ �˵���ͼ �� ������ͼ������Ӧ�ķŴ���С����Ч
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		//�õ��˵����ĵ�������������ֵ�����䷶ΧΪ����0.0~1.0֮��
		float scale = l * 1.0f / mMenuWidth;
		//��ߵ����ųߴ磬
		float leftScale = 1 - 0.3f * scale;
		//�ұ߳ߴ�
		float rightScale = 0.8f + scale * 0.2f;
        
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		//����͸���ȣ��ڻ���������͸�����𽥼����䵭���������õ��Ǵ�0.0~1֮��仯����0.0��Ϊ0.5��������0.5~1.0֮��仯
		//ע�⣺���0.8���ϵ����Ҫ����ʵ������޸ģ������0.8��Ϊ.05�ᷢ�ֵ��˵���ȫչʾʱ��͸���Ȳ�̫�ߣ�
		ViewHelper.setAlpha(mMenu, 0.0f + 0.8f * (1 - scale));
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.6f);
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);
	}
}
