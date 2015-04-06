package com.scho.note.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.note.ui.activity.R;
import com.scho.note.Constants;
import com.scho.note.basic.activity.BaseMainUIActivity;
import com.scho.note.utils.SPUtils;


public class GuideActivity extends BaseMainUIActivity implements
		OnPageChangeListener {
	/** ViewPager实例 */
	private ViewPager mViewPager;
	/** ViewPager适配器 */
	private GuideViewPagerAdapter mViewPagerAdapter;
	/** 适配器数据 */
	private List<View> mViewLists;
	/** ImageView视图数组 */
	private ImageView[] mGuidDots;
	/** 当前引用页的下标 */
	private int currentIndex;

	@Override
	public void initCompontent() {
		super.initCompontent();
		setContentView(R.layout.layout_guide);
		// 初始化View控件
		initViews();
		// 初始化
		initDots();
	}

	@Override
	public void initAdapter() {
		super.initAdapter();
	}

	@Override
	public void initSourceData() {
		super.initSourceData();

	}

	@Override
	public void initListener() {
		super.initListener();
	}
    /**
     * 
     * Description: 初始化View列表
     * @author: liwei
     * @date:2015年2月12日 下午1:05:58
     */
	@SuppressLint("NewApi")
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);
		mViewLists = new ArrayList<View>();
		// 添加ImageView实例到数组
		mViewLists.add(inflater.inflate(R.layout.layout_guide_one, null));
		mViewLists.add(inflater.inflate(R.layout.layout_guide_two, null));
		mViewLists.add(inflater.inflate(R.layout.layout_guide_three, null));
		mViewLists.add(inflater.inflate(R.layout.layout_guide_four, null));
		// ImageView适配器
		mViewPagerAdapter = new GuideViewPagerAdapter(mViewLists, this);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		// 设置适配器
		mViewPager.setAdapter(mViewPagerAdapter);
		// 设置监听器
		mViewPager.setOnPageChangeListener(this);
	}

	private void initDots() {
		// 获取线性布局对象
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
		mGuidDots = new ImageView[mViewLists.size()];
		// 循环遍历对象
		for (int i = 0; i < mViewLists.size(); i++) {
			// Returns the view at the specified position in the group.
			// 返回指定位置的View，
			mGuidDots[i] = (ImageView) ll.getChildAt(i);
			mGuidDots[i].setEnabled(true);// 激活该视图
		}
		// 初始化时设置当前引导页下标为0
		currentIndex = 0;
		mGuidDots[currentIndex].setEnabled(false);// 将当前的视图不激活
	}
    /**
     * 
     * Description: 设置当前页面的圆点
     * @author: liwei
     * @date:2015年2月12日 下午1:04:51
     * @param position
     */
	private void setCurrentDot(int position) {
		if (position < 0 || position > mViewLists.size() - 1
				|| currentIndex == position) {
			return;
		}

		mGuidDots[position].setEnabled(false);
		// 激活当前页面
		mGuidDots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

	//
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	//
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 页面改变监听事件
	@Override
	public void onPageSelected(int arg0) {
		setCurrentDot(arg0);
	}
	/**
	 * 
	 * @author liwei
	 * @Description: 引导界面的ViewPager适配器 
	 * @date:2015年2月12日 下午1:40:54
	 */
	private class GuideViewPagerAdapter extends PagerAdapter {

		private List<View> views;
        private Context context;
		public GuideViewPagerAdapter(List<View> views, Context context) {
			this.views = views;
			this.context = context;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}
		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			if (arg1 == views.size() - 1) {
				Button mStartWeiboImageButton = (Button) arg0
						.findViewById(R.id.iv_start_weibo);
				mStartWeiboImageButton.setOnClickListener(new OnClickListener() { 
					@Override
					public void onClick(View v) {
						
						//引导功能处于开发阶段，开发完毕再取消注释，put到sp对象
						SPUtils.put(mContext,Constants.EnterModeCode.SP_KEY_ISFIRST_USE, true);
						goHome();

					}

				});
			}
			return views.get(arg1);
		}
        
		private void goHome() {
			Intent intent = new Intent(context, MainActivity.class);
			context.startActivity(intent);
			GuideActivity.this.finish();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}
}
