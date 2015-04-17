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
     * �����¼�����
     */
	private void initListener() {
		ClickListener listener = new ClickListener();
		mSchoolProfile.setOnClickListener(listener);
		mSchoolDepartment.setOnClickListener(listener);
		mSchoolScenery.setOnClickListener(listener);
	}
	/**
	 * ��Ƭ֮����л�
	 * @param view
	 */
	public void changedRadioButtonByClick(View view) {
		// ȡ��FM����
		FragmentManager fm = mFragementActivity.getSupportFragmentManager();
		// �������֮��һ��Ҫ�ǵ��ύ������add��Ч
		FragmentTransaction ft = fm.beginTransaction();
		if (fm.getFragments() != null) {
			for (Fragment fragment : fm.getFragments()) {
				// ���û�����ؾ�����֮
				if (!fragment.isHidden()) {
					ft.hide(fragment);
				}
			}
		}
		switch (view.getId()) {
		//У԰���
		case R.id.school_profile:
			SchoolProfilesFragment profileFragment = (SchoolProfilesFragment) fm
					.findFragmentByTag("schol_profile");
			if (profileFragment == null) {
				profileFragment = SchoolProfilesFragment.getInstance();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, profileFragment, "schol_profile");
			}
			// ��ʾ���
			ft.show(profileFragment);
			break;
			//У԰�羰
		case R.id.school_scenery:
			SchoolSceneryFragment reportFragment = (SchoolSceneryFragment) fm
					.findFragmentByTag("report_f");
			if (reportFragment == null) {
				reportFragment = SchoolSceneryFragment.getInstance();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, reportFragment, "report_f");
			}
			// ��ʾ�򿨽���
			ft.show(reportFragment);
			break;
			//Ժϵ����
		case R.id.school_department:
			SchoolDepartmentFragment relationFragment = (SchoolDepartmentFragment) fm
					.findFragmentByTag("relation_f");
			if (relationFragment == null) {
				relationFragment = SchoolDepartmentFragment.getInstance();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, relationFragment, "relation_f");
			}
			// ��ʾ������ҳ
			ft.show(relationFragment);
			break;
		default:
			break;
			
		}
		ft.commitAllowingStateLoss();
	}

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
			case R.id.school_profile:
//				mSchoolProfile.setTextColor(color);
				changedRadioButtonByClick(v);
				break;
			// У԰�羰
			case R.id.school_scenery:
				changedRadioButtonByClick(v);
				break;
			// Ժϵ����
			case R.id.school_department:
				changedRadioButtonByClick(v);
				break;
			default:
				break;
			}

		}

	}

}
