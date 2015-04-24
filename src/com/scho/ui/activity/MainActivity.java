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
	/** 首页 */
	private RadioButton mHomeRadioButton;
	/** 校园活动 */
	private RadioButton mSchoolEventsButton;
	/** 天气情况*/
	private RadioButton mWeatherButton;
	/** 个人中心*/
	private RadioButton mPersonalCenterButton;
	/** 当前展示的RadioButton */
	private RadioButton mFirstPageLayout;
	/** 当前用户*/
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
	 * 初始化ActionBar
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
	 * 初始化界面碎片
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
				//先判断用户是否登陆
					changedRadioButtonByClick(mSchoolEventsButton);
			}
		});
		mWeatherButton = (RadioButton) findViewById(R.id.weather_btn);
		mWeatherButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//先判断用户是否登陆
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
		//当用户进入应用时首先展示的是首页界面
			changedRadioButtonByClick(mHomeRadioButton);
			
	}

	/**
	 * 
	 * Description: 单机RadioButton时不同Fragment之间跳转，这里实现底部四个Fragment的初始化以及获取
	 * 
	 * @author: 李伟
	 * @date:2015年2月15日 上午12:52:35
	 */
	public void changedRadioButtonByClick(RadioButton buttonView) {
		if (buttonView == mFirstPageLayout)
			return;
		// 取得FM对象
		FragmentManager fm = getSupportFragmentManager();
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
		switch (buttonView.getId()) {
		//首页
		case R.id.home_btn:
			HomeFragment homeFragment = (HomeFragment) fm
					.findFragmentByTag("home_f");
			if (homeFragment == null) {
				homeFragment = HomeFragment.getInstance();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, homeFragment,"home_f");
			}
			// 显示首页
			ft.show(homeFragment);
			break;
			//校园活动
		case R.id.school_events_btn:
			SchoolEventsFragment reportFragment = (SchoolEventsFragment) fm
					.findFragmentByTag("events_f");
			if (reportFragment == null) {
				reportFragment = SchoolEventsFragment.getInstance();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, reportFragment,"events_f");
			}
			// 显示打卡界面
			ft.show(reportFragment);
			break;
			//最近天气
		case R.id.weather_btn:
			WeatherFragment relationFragment = (WeatherFragment) fm
					.findFragmentByTag("weather_f");
			if (relationFragment == null) {
				relationFragment = WeatherFragment.getInstance();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, relationFragment,"weather_f");
			}
			// 显示人脉首页
			ft.show(relationFragment);
			break;
			//个人中心
		case R.id.personal_center_btn:
			PersonalCenterFragment discoverFragment = (PersonalCenterFragment) fm
					.findFragmentByTag("center_f");
			if (discoverFragment == null) {
				discoverFragment = PersonalCenterFragment.getInstance();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, discoverFragment,"center_f");
			}
			// 显示发现界面
			ft.show(discoverFragment);
			break;
		default:
			break;
			
		}
		ft.commitAllowingStateLoss();
	}

}
