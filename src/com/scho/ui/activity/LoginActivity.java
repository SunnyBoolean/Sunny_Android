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
 * @Description: 用户登录界面
 * @date: 2015年4月16日
 */
public class LoginActivity extends Activity {
	/** 上下文*/
	private Context mContext;
	/** 用户名 */
	private EditText mUserNmaeEt;
	/** 用户密码 */
	private EditText mUserPasswordEt;
	/** 登录 */
	private Button mLoginBtn;
	/** 注册 */
	private Button mRegsinBtn;
	/** 登陆进度条*/
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
		mProgressLogin.setMessage("正在登陆...");
		initListener();
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		ClickListener listener = new ClickListener();
		mLoginBtn.setOnClickListener(listener);
		mRegsinBtn.setOnClickListener(listener);
	}
	/**
	 * 读取用户登陆信息
	 */
private void readInput(){
	AVUser avUser = new AVUser();
	//获取用户名
	String userName = mUserNmaeEt.getText().toString();
	//读取用户密码
	String userPassword = mUserPasswordEt.getText().toString();
	AVUser.logInInBackground(userName, userPassword, new LogInCallback() {
		public void done(AVUser user, AVException e) {
	        if (user != null) {
	            // 登录成功跳转到主界面
	        	Intent intent = new Intent();
		        intent.setClass(mContext, MainActivity.class);
//		        intent.putExtra("sina_user", mUserInfoCV);
		        mProgressLogin.dismiss();
		        startActivity(intent);
		        finish();
	        } else {
	        	mProgressLogin.dismiss();
	            Toast.makeText(mContext, "登陆失败，请检查密码或用户名！", Toast.LENGTH_SHORT).show();
	        }
	    }
	});
}
	/**
	 * 单击事件监听
	 * 
	 * @author: hello
	 * @Description: TODO
	 * @date: 2015年4月16日
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
