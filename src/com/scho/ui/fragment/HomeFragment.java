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
 * @Description: ��ҳ��Ƭ����
 * @date: 2015��4��16��
 */
public class HomeFragment extends Fragment {
	/** ��Ƭʵ�� */
	private static HomeFragment mInstance;
	/** �����Ļ��� */
	private Context mContext;
	/** ͼƬ */
	private DisplayImageOptions mDisplayImageOptions;
	/** ԺУ��� */
	private TextView mSchoolProfile;
	/** У԰�羰 */
	private TextView mSchoolScenery;
	/** Ժϵ���� */
	private TextView mSchoolDepartment;
	/** FrameLayout���� */
	private FrameLayout mSchoolProfileContener;
	/** FragmentActivityʵ��*/
	private FragmentActivity mFragementActivity;
	/** ViewPager*/
	private ViewPager mContenerViewPager;
	/** ��Ƭ������*/
	private FragmentsAdapter mAdapter;
	/** ָʾ��*/
	private TabPageIndicator mIndicator;
	// ��ȡ��ҳ��Ƭʵ��
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
				.showImageOnFail(R.drawable.placeholder_image) // ����ͼƬ����ʧ�ܵ�չʾͼƬ
				.displayer(new RoundedBitmapDisplayer(35)) // ����Բ��ͼƬ
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
		//���û�����ʱ����չʾԺУ���
//		changedRadioButtonByClick(mSchoolProfile);
		initAdapter();
//		initListener();
		return view;
	}
    /**
     * �����¼�����
     */
	private void initListener() {
		//��������
		ClickListener listener = new ClickListener();
		//ViewPager����
		PageChangedListener vlistener = new PageChangedListener();
		mSchoolProfile.setOnClickListener(listener);
		mSchoolDepartment.setOnClickListener(listener);
		mSchoolScenery.setOnClickListener(listener);
		mContenerViewPager.setOnPageChangeListener(vlistener);
	}
	/**
	 * ��ʼ��������
	 */
	public void initAdapter() {
		mAdapter = new FragmentsAdapter(getChildFragmentManager());
		// ΪViewPager����������
		mContenerViewPager.setAdapter(mAdapter);
		// Ԥ����һ��ҳ��
		mContenerViewPager.setOffscreenPageLimit(1);
		// Ϊָʾ������ViewPager
		mIndicator.setViewPager(mContenerViewPager);
	}
	/**
	 * ViewPager������
	 * @author:  liwei
	 * @Description:  TODO 
	 * @date:  2015��4��18��
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
	 * @author:  ԺУ���
	 * @Description:  TODO 
	 * @date:  2015��4��18��
	 */
	private class FragmentsAdapter extends FragmentPagerAdapter {

		/**
		 * Tab����
		 */
		protected final String[] CONTENT = new String[] { "ѧУ���","У԰�羰","ѧУ����" };

		private int mCount = CONTENT.length;
		/**
		 * Fragment����
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
	 * ��Ƭ֮����л�
	 * @param view
//	 */
//	public void changedRadioButtonByClick(View view) {
//		// ȡ��FM����
//		FragmentManager fm = mFragementActivity.getSupportFragmentManager();
//		// �������֮��һ��Ҫ�ǵ��ύ������add��Ч
//		FragmentTransaction ft = fm.beginTransaction();
//		if (fm.getFragments() != null) {
//			for (Fragment fragment : fm.getFragments()) {
//				// ���û�����ؾ�����֮
//				if (!fragment.isHidden()) {
//					ft.hide(fragment);
//				}
//			}
//		}
//		switch (view.getId()) {
//		//У԰���
//		case R.id.school_profile:
//			SchoolProfilesFragment profileFragment = (SchoolProfilesFragment) fm
//					.findFragmentByTag("schol_profile");
//			if (profileFragment == null) {
//				profileFragment = SchoolProfilesFragment.getInstance();
//				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
//				ft.add(R.id.schol_home, profileFragment, "schol_profile");
//			}
//			// ��ʾ���
//			ft.show(profileFragment);
//			break;
//			//У԰�羰
//		case R.id.school_scenery:
//			SchoolSceneryFragment reportFragment = (SchoolSceneryFragment) fm
//					.findFragmentByTag("scho_scenery");
//			if (reportFragment == null) {
//				reportFragment = SchoolSceneryFragment.getInstance();
//				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
//				ft.add(R.id.schol_home, reportFragment, "scho_scenery");
//			}
//			// ��ʾ�򿨽���
//			ft.show(reportFragment);
//			break;
//			//Ժϵ����
//		case R.id.school_department:
//			SchoolDepartmentFragment relationFragment = (SchoolDepartmentFragment) fm
//					.findFragmentByTag("scho_depart");
//			if (relationFragment == null) {
//				relationFragment = SchoolDepartmentFragment.getInstance();
//				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
//				ft.add(R.id.schol_home, relationFragment, "scho_depart");
//			}
//			// ��ʾ������ҳ
//			ft.show(relationFragment);
//			break;
//		default:
//			break;
//			
//		}
//		ft.commitAllowingStateLoss();
//	}

	/**
	 * �����¼�
	 * 
	 * @author: liwei
	 * @Description: ��ͼ�ĵ����¼�����
	 * @date: 2015��4��17��
	 */
	private class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// У԰���
//			case R.id.school_profile:
////				mSchoolProfile.setTextColor(color);
////				changedRadioButtonByClick(v);
//				break;
//			// У԰�羰
//			case R.id.school_scenery:
////				changedRadioButtonByClick(v);
//				break;
//			// Ժϵ����
//			case R.id.school_department:
//				changedRadioButtonByClick(v);
//				break;
			default:
				break;
			}

		}

	}

}
