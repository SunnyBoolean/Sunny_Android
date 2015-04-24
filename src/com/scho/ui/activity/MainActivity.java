package com.scho.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.avos.avoscloud.AVUser;
import com.scho.ui.R;
import com.scho.ui.fragment.HomeFragment;
import com.scho.ui.fragment.PersonalCenterFragment;
import com.scho.ui.fragment.SchoolEventsFragment;
import com.scho.ui.fragment.WeatherFragment;

public class MainActivity extends FragmentActivity {
	/** ��ҳ */
	private RadioButton mHomeRadioButton;
	/** У԰� */
	private RadioButton mSchoolEventsButton;
	/** �������*/
	private RadioButton mWeatherButton;
	/** ��������*/
	private RadioButton mPersonalCenterButton;
	/** ��ǰչʾ��RadioButton */
	private RadioButton mFirstPageLayout;
	/** ��ǰ�û�*/
	private AVUser mCurAvuser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mHomeRadioButton = (RadioButton) findViewById(R.id.home_btn);
		mSchoolEventsButton = (RadioButton) findViewById(R.id.school_events_btn);
		mWeatherButton = (RadioButton) findViewById(R.id.weather_btn);
		mPersonalCenterButton = (RadioButton) findViewById(R.id.personal_center_btn);
		initFragementView();
		mCurAvuser = AVUser.getCurrentUser();
	}
	/**
	 * ��ʼ��ActionBar
	 */
    public void initActionBar(){
    	
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * ��ʼ��������Ƭ
	 */
	public void initFragementView() {
		mHomeRadioButton = (RadioButton) findViewById(R.id.home_btn);
		mHomeRadioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changedRadioButtonByClick(mHomeRadioButton);
			}
		});
		mSchoolEventsButton = (RadioButton) findViewById(R.id.school_events_btn);
		mSchoolEventsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//���ж��û��Ƿ��½
					changedRadioButtonByClick(mSchoolEventsButton);
			}
		});
		mWeatherButton = (RadioButton) findViewById(R.id.weather_btn);
		mWeatherButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//���ж��û��Ƿ��½
				changedRadioButtonByClick(mWeatherButton);
			}
		});
		mPersonalCenterButton = (RadioButton) findViewById(R.id.personal_center_btn);
		mPersonalCenterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changedRadioButtonByClick(mPersonalCenterButton);
			}
		});
		//���û�����Ӧ��ʱ����չʾ������ҳ����
			changedRadioButtonByClick(mHomeRadioButton);
			
	}

	/**
	 * 
	 * Description: ����RadioButtonʱ��ͬFragment֮����ת������ʵ�ֵײ��ĸ�Fragment�ĳ�ʼ���Լ���ȡ
	 * 
	 * @author: ��ΰ
	 * @date:2015��2��15�� ����12:52:35
	 */
	public void changedRadioButtonByClick(RadioButton buttonView) {
		if (buttonView == mFirstPageLayout)
			return;
		// ȡ��FM����
		FragmentManager fm = getSupportFragmentManager();
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
		switch (buttonView.getId()) {
		//��ҳ
		case R.id.home_btn:
			HomeFragment homeFragment = (HomeFragment) fm
					.findFragmentByTag("home_f");
			if (homeFragment == null) {
				homeFragment = HomeFragment.getInstance();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, homeFragment,"home_f");
			}
			// ��ʾ��ҳ
			ft.show(homeFragment);
			break;
			//У԰�
		case R.id.school_events_btn:
			SchoolEventsFragment reportFragment = (SchoolEventsFragment) fm
					.findFragmentByTag("events_f");
			if (reportFragment == null) {
				reportFragment = SchoolEventsFragment.getInstance();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, reportFragment,"events_f");
			}
			// ��ʾ�򿨽���
			ft.show(reportFragment);
			break;
			//�������
		case R.id.weather_btn:
			WeatherFragment relationFragment = (WeatherFragment) fm
					.findFragmentByTag("weather_f");
			if (relationFragment == null) {
				relationFragment = WeatherFragment.getInstance();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, relationFragment,"weather_f");
			}
			// ��ʾ������ҳ
			ft.show(relationFragment);
			break;
			//��������
		case R.id.personal_center_btn:
			PersonalCenterFragment discoverFragment = (PersonalCenterFragment) fm
					.findFragmentByTag("center_f");
			if (discoverFragment == null) {
				discoverFragment = PersonalCenterFragment.getInstance();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, discoverFragment,"center_f");
			}
			// ��ʾ���ֽ���
			ft.show(discoverFragment);
			break;
		default:
			break;
			
		}
		ft.commitAllowingStateLoss();
	}

}
