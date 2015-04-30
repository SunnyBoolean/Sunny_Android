package com.scho.ui.activity;

import com.avos.avoscloud.AVUser;
import com.scho.ui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * @author liwei
 * @Description: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 *               (2)是，则进入GuideActivity；否，则进入MainActivity (3)3s后执行(2)操作
 * @date:2015年2月12日 下午1:07:11
 */
public class SplashActivity extends Activity {

	/** 是否是第一次使用 */
	boolean isFirstIn = false;

	/** 直接跳转到home页还是引导页标志 */
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;

	/** 延迟引导时间 */
	private static final long SPLASH_DELAY_MILLIS = 2000;

	/**
	 * Handler:跳转到不同的界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_guide_splash);
		initGuideSP();
	}

	/**
	 * 
	 * Description: 初始化
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午1:08:49
	 */
	private void initGuideSP() {
		
		
		AVUser mCurUser = AVUser.getCurrentUser();
		if (mCurUser != null) {
			String name = mCurUser.getUsername();
			String id = mCurUser.getUuid();
			if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id)) {
//				goHome();
				mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
			} else {
//				goGuide();
				mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
			}
		} else {
			goGuide();
		}
	}

	private void goHome() {
		// Intent intent = new Intent(this, MainActivity.class);

		Intent intent = new Intent(this, MainActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(this, LoginActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}
}
