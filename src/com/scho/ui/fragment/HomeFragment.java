package com.scho.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scho.ui.R;
import com.viewpagerindicator.TabPageIndicator;

/**
 * @author: liwei
 * @Description: 首页碎片界面
 * @date: 2015年4月16日
 */
public class HomeFragment extends Fragment {
	/** 碎片实例 */
	private static HomeFragment mInstance;
	/** 上下文环境 */
	private Context mContext;
	/** 图片 */
	private DisplayImageOptions mDisplayImageOptions;
	/** 院校简介 */
	private TextView mSchoolProfile;
	/** 校园风景 */
	private TextView mSchoolScenery;
	/** 院系设置 */
	private TextView mSchoolDepartment;
	/** FrameLayout容器 */
	private FrameLayout mSchoolProfileContener;
	/** FragmentActivity实例*/
	private FragmentActivity mFragementActivity;
	/** ViewPager*/
	private ViewPager mContenerViewPager;
	/** 碎片适配器*/
	private FragmentsAdapter mAdapter;
	/** 指示器*/
	private TabPageIndicator mIndicator;
	// 获取首页碎片实例
	public static HomeFragment getInstance() {
		if (mInstance == null) {
			mInstance = new HomeFragment();
		}
		return mInstance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		mContext = activity;
		mFragementActivity = (FragmentActivity) activity;
		mDisplayImageOptions = new DisplayImageOptions.Builder().cacheOnDisc()
				.cacheInMemory().showStubImage(R.drawable.placeholder_image)
				.showImageOnFail(R.drawable.placeholder_image) // 设置图片加载失败的展示图片
				.displayer(new RoundedBitmapDisplayer(35)) // 设置圆角图片
				.showImageForEmptyUri(R.drawable.placeholder_image).build();
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_home, null);
//		mSchoolProfile = (TextView) view.findViewById(R.id.school_profile);
//		mSchoolDepartment = (TextView) view
//				.findViewById(R.id.school_department);
//		mSchoolScenery = (TextView) view.findViewById(R.id.school_scenery);
		mContenerViewPager = (ViewPager) view.findViewById(R.id.schol_viewpager);
		mIndicator = (TabPageIndicator) view.findViewById(R.id.scho_indicator);
//		mSchoolProfileContener = (FrameLayout) view
//				.findViewById(R.id.schol_home);
		//当用户进来时首先展示院校简介
//		changedRadioButtonByClick(mSchoolProfile);
		initAdapter();
//		initListener();
		return view;
	}
    /**
     * 单击事件监听
     */
	private void initListener() {
		//单击监听
		ClickListener listener = new ClickListener();
		//ViewPager监听
		PageChangedListener vlistener = new PageChangedListener();
		mSchoolProfile.setOnClickListener(listener);
		mSchoolDepartment.setOnClickListener(listener);
		mSchoolScenery.setOnClickListener(listener);
		mContenerViewPager.setOnPageChangeListener(vlistener);
	}
	/**
	 * 初始化适配器
	 */
	public void initAdapter() {
		mAdapter = new FragmentsAdapter(getChildFragmentManager());
		// 为ViewPager设置适配器
		mContenerViewPager.setAdapter(mAdapter);
		// 预加载一个页面
		mContenerViewPager.setOffscreenPageLimit(1);
		// 为指示器设置ViewPager
		mIndicator.setViewPager(mContenerViewPager);
	}
	/**
	 * ViewPager监听器
	 * @author:  liwei
	 * @Description:  TODO 
	 * @date:  2015年4月18日
	 */
	private class PageChangedListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 
	 * @author:  院校简介
	 * @Description:  TODO 
	 * @date:  2015年4月18日
	 */
	private class FragmentsAdapter extends FragmentPagerAdapter {

		/**
		 * Tab标题
		 */
		protected final String[] CONTENT = new String[] { "学校简介","校园风景","学校官网" };

		private int mCount = CONTENT.length;
		/**
		 * Fragment容器
		 */
		private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

		public FragmentsAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			mFragments.add(SchoolProfilesFragment.getInstance());
			mFragments.add(SchoolSceneryFragment.getInstance());
			mFragments.add(SchoolDepartmentFragment.getInstance());
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return mCount;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length];
		}

		public void setCount(int count) {
			if (count > 0 && count <= 10) {
				mCount = count;
				notifyDataSetChanged();
			}
		}
	}
	/**
	 * 碎片之间的切换
	 * @param view
//	 */
//	public void changedRadioButtonByClick(View view) {
//		// 取得FM对象
//		FragmentManager fm = mFragementActivity.getSupportFragmentManager();
//		// 开启事物，之后一定要记得提交，否则add无效
//		FragmentTransaction ft = fm.beginTransaction();
//		if (fm.getFragments() != null) {
//			for (Fragment fragment : fm.getFragments()) {
//				// 如果没有隐藏就隐藏之
//				if (!fragment.isHidden()) {
//					ft.hide(fragment);
//				}
//			}
//		}
//		switch (view.getId()) {
//		//校园简介
//		case R.id.school_profile:
//			SchoolProfilesFragment profileFragment = (SchoolProfilesFragment) fm
//					.findFragmentByTag("schol_profile");
//			if (profileFragment == null) {
//				profileFragment = SchoolProfilesFragment.getInstance();
//				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
//				ft.add(R.id.schol_home, profileFragment, "schol_profile");
//			}
//			// 显示简介
//			ft.show(profileFragment);
//			break;
//			//校园风景
//		case R.id.school_scenery:
//			SchoolSceneryFragment reportFragment = (SchoolSceneryFragment) fm
//					.findFragmentByTag("scho_scenery");
//			if (reportFragment == null) {
//				reportFragment = SchoolSceneryFragment.getInstance();
//				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
//				ft.add(R.id.schol_home, reportFragment, "scho_scenery");
//			}
//			// 显示打卡界面
//			ft.show(reportFragment);
//			break;
//			//院系设置
//		case R.id.school_department:
//			SchoolDepartmentFragment relationFragment = (SchoolDepartmentFragment) fm
//					.findFragmentByTag("scho_depart");
//			if (relationFragment == null) {
//				relationFragment = SchoolDepartmentFragment.getInstance();
//				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
//				ft.add(R.id.schol_home, relationFragment, "scho_depart");
//			}
//			// 显示人脉首页
//			ft.show(relationFragment);
//			break;
//		default:
//			break;
//			
//		}
//		ft.commitAllowingStateLoss();
//	}

	/**
	 * 单击事件
	 * 
	 * @author: liwei
	 * @Description: 视图的单击事件监听
	 * @date: 2015年4月17日
	 */
	private class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 校园简介
//			case R.id.school_profile:
////				mSchoolProfile.setTextColor(color);
////				changedRadioButtonByClick(v);
//				break;
//			// 校园风景
//			case R.id.school_scenery:
////				changedRadioButtonByClick(v);
//				break;
//			// 院系设置
//			case R.id.school_department:
//				changedRadioButtonByClick(v);
//				break;
			default:
				break;
			}

		}

	}

}
