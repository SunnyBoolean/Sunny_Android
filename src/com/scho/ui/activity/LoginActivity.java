/**
 * 
 */
package com.scho.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.scho.ui.R;
import com.scho.ui.activity.RegisterActivity.ClickListener;

/**
 * @author: liwei
 * @Description: �û���¼����
 * @date: 2015��4��16��
 */
public class LoginActivity extends Activity {
	/** �û��� */
	private EditText mUserNmaeEt;
	/** �û����� */
	private EditText mUserPasswordEt;
	/** ��¼ */
	private Button mLoginBtn;
	/** ע�� */
	private Button mRegsinBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		mUserNmaeEt = (EditText) findViewById(R.id.user_name);
		mUserPasswordEt = (EditText) findViewById(R.id.user_password);
		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mRegsinBtn = (Button) findViewById(R.id.regsiter_btn);
		initListener();
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		ClickListener listener = new ClickListener();
		mLoginBtn.setOnClickListener(listener);
		mRegsinBtn.setOnClickListener(listener);
	}

	/**
	 * �����¼�����
	 * 
	 * @author: hello
	 * @Description: TODO
	 * @date: 2015��4��16��
	 */
	private class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_btn:

				break;
			case R.id.regsiter_btn:

				break;
			default:
				break;
			}
		}

	}

}
