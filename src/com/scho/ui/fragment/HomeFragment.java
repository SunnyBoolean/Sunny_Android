/**
 * 
 */
package com.scho.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
		mSchoolProfile = (TextView) view.findViewById(R.id.school_profile);
		mSchoolDepartment = (TextView) view
				.findViewById(R.id.school_department);
		mSchoolScenery = (TextView) view.findViewById(R.id.school_scenery);
		mSchoolProfileContener = (FrameLayout) view
				.findViewById(R.id.schol_home);
		initListener();
		return view;
	}
    /**
     * 单击事件监听
     */
	private void initListener() {
		ClickListener listener = new ClickListener();
		mSchoolProfile.setOnClickListener(listener);
		mSchoolDepartment.setOnClickListener(listener);
		mSchoolScenery.setOnClickListener(listener);
	}
	/**
	 * 碎片之间的切换
	 * @param view
	 */
	public void changedRadioButtonByClick(View view) {
		// 取得FM对象
		FragmentManager fm = mFragementActivity.getSupportFragmentManager();
		// 开启事物，之后一定要记得提交，否则add无效
		FragmentTransaction ft = fm.beginTransaction();
		if (fm.getFragments() != null) {
			for (Fragment fragment : fm.getFragments()) {
				// 如果没有隐藏就隐藏之
				if (!fragment.isHidden()) {
					ft.hide(fragment);
				}
			}
		}
		switch (view.getId()) {
		//校园简介
		case R.id.school_profile:
			SchoolProfilesFragment profileFragment = (SchoolProfilesFragment) fm
					.findFragmentByTag("schol_profile");
			if (profileFragment == null) {
				profileFragment = SchoolProfilesFragment.getInstance();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, profileFragment, "schol_profile");
			}
			// 显示简介
			ft.show(profileFragment);
			break;
			//校园风景
		case R.id.school_scenery:
			SchoolSceneryFragment reportFragment = (SchoolSceneryFragment) fm
					.findFragmentByTag("report_f");
			if (reportFragment == null) {
				reportFragment = SchoolSceneryFragment.getInstance();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, reportFragment, "report_f");
			}
			// 显示打卡界面
			ft.show(reportFragment);
			break;
			//院系设置
		case R.id.school_department:
			SchoolDepartmentFragment relationFragment = (SchoolDepartmentFragment) fm
					.findFragmentByTag("relation_f");
			if (relationFragment == null) {
				relationFragment = SchoolDepartmentFragment.getInstance();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, relationFragment, "relation_f");
			}
			// 显示人脉首页
			ft.show(relationFragment);
			break;
		default:
			break;
			
		}
		ft.commitAllowingStateLoss();
	}

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
			case R.id.school_profile:
//				mSchoolProfile.setTextColor(color);
				changedRadioButtonByClick(v);
				break;
			// 校园风景
			case R.id.school_scenery:
				changedRadioButtonByClick(v);
				break;
			// 院系设置
			case R.id.school_department:
				changedRadioButtonByClick(v);
				break;
			default:
				break;
			}

		}

	}

}
