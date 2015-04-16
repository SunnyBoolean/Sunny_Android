/**
 * 
 */
package com.scho.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.scho.ui.R;

/**
 * @author: liwei
 * @Description: �û���¼����
 * @date: 2015��4��16��
 */
public class LoginActivity extends Activity {
	/** ������*/
	private Context mContext;
	/** �û��� */
	private EditText mUserNmaeEt;
	/** �û����� */
	private EditText mUserPasswordEt;
	/** ��¼ */
	private Button mLoginBtn;
	/** ע�� */
	private Button mRegsinBtn;
	/** ��½������*/
	private  ProgressDialog mProgressLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		mUserNmaeEt = (EditText) findViewById(R.id.user_name);
		mUserPasswordEt = (EditText) findViewById(R.id.user_password);
		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mRegsinBtn = (Button) findViewById(R.id.regsiter_btn);
		mProgressLogin = new ProgressDialog(mContext);
		mProgressLogin.setMessage("���ڵ�½...");
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
	 * ��ȡ�û���½��Ϣ
	 */
private void readInput(){
	AVUser avUser = new AVUser();
	//��ȡ�û���
	String userName = mUserNmaeEt.getText().toString();
	//��ȡ�û�����
	String userPassword = mUserPasswordEt.getText().toString();
	AVUser.logInInBackground(userName, userPassword, new LogInCallback() {
		public void done(AVUser user, AVException e) {
	        if (user != null) {
	            // ��¼�ɹ���ת��������
	        	Intent intent = new Intent();
		        intent.setClass(mContext, MainActivity.class);
//		        intent.putExtra("sina_user", mUserInfoCV);
		        mProgressLogin.dismiss();
		        startActivity(intent);
		        finish();
	        } else {
	        	mProgressLogin.dismiss();
	            Toast.makeText(mContext, "��½ʧ�ܣ�����������û�����", Toast.LENGTH_SHORT).show();
	        }
	    }
	});
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
				mProgressLogin.show();
				readInput();
				break;
			case R.id.regsiter_btn:

				break;
			default:
				break;
			}
		}

	}

}
