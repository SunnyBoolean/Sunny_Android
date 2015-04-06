/**
 * 
 */
package com.scho.note.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.note.ui.activity.R;

/**
 * @author ��ΰ
 * @Description:TODO
 * @date:2015��3��7�� ����2:50:39
 */

public class UserLoginActivity extends Activity {
	private TextView mUserNameTV;
	private TextView mUserPasswordTV;
	private Button mLogin;
	private Button mLoginCancel;
	private Button mSinaLogin;
	private Button mQQLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_login);
		// ��ȡ���ʵ��
		getCompontent();
	}

	/**
	 * 
	 * Description: ��xml��ȡ���ʵ��
	 * 
	 * @author: ��ΰ']
	 * @date:2015��3��7�� ����3:29:48
	 */
	public void getCompontent() {
		mUserNameTV = (TextView) findViewById(R.id.user_name);
		mUserPasswordTV = (TextView) findViewById(R.id.user_password);
		mLoginCancel = (Button) findViewById(R.id.login_cancel);
		mLogin = (Button) findViewById(R.id.login);
		mSinaLogin = (Button) findViewById(R.id.sina_login);
		mQQLogin = (Button) findViewById(R.id.qq_login);
	}

	private class SingleClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login:

				break;
			case R.id.login_cancel:

				break;
			case R.id.sina_login:

				break;
			case R.id.qq_login:

				break;

			default:
				break;
			}
		}

	}

}
