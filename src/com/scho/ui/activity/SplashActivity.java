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
 * @Description: �������� (1)�ж��Ƿ����״μ���Ӧ��--��ȡ��ȡSharedPreferences�ķ���
 *               (2)�ǣ������GuideActivity���������MainActivity (3)3s��ִ��(2)����
 * @date:2015��2��12�� ����1:07:11
 */
public class SplashActivity extends Activity {

	/** �Ƿ��ǵ�һ��ʹ�� */
	boolean isFirstIn = false;

	/** ֱ����ת��homeҳ��������ҳ��־ */
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;

	/** �ӳ�����ʱ�� */
	private static final long SPLASH_DELAY_MILLIS = 2000;

	/**
	 * Handler:��ת����ͬ�Ľ���
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
	 * Description: ��ʼ��
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����1:08:49
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
