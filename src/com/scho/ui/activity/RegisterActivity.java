/**
 * 
 */
package com.scho.ui.activity;

import com.scho.ui.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.scho.ui.R;
import com.scho.utils.NoteUtil;

/**
 * @author: liwei
 * @Description: 注册界面
 * @date: 2015年4月16日
 */
public class RegisterActivity extends Activity {
	/** 上下文*/
	private Context mContext;
	/** 邮箱*/
	private EditText mUserEmialEt;
	/** 用户名 */
	private EditText mUserNmaeEt;
	/** 用户密码 */
	private EditText mUserPasswordEt;
	/** 确认密码*/
	private EditText mUserPasswordConfirm;
	/** 登录 */
	private Button mLoginBtn;
	/** 注册 */
	private Button mCancelBtn;
	/** 注册进度*/
	private ProgressDialog mProgressLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register);
		mContext = this;
		mUserEmialEt = (EditText) findViewById(R.id.user_email);
	    mUserNmaeEt = (EditText) findViewById(R.id.user_name);
	    mUserPasswordEt = (EditText) findViewById(R.id.user_password);
	    mUserPasswordConfirm = (EditText) findViewById(R.id.confirm_password);
	    mLoginBtn = (Button) findViewById(R.id.regsiter_btn);
	    mCancelBtn = (Button) findViewById(R.id.cancel_reg);
	    mProgressLogin = new ProgressDialog(mContext);
	    mProgressLogin.setMessage("注册中...");
		initListener();
	}
	/**
	 * 读取注册信息
	 */
	private void readInput(){
		AVUser avUser = new AVUser();
		String userEmail = mUserEmialEt.getText().toString();
		final String userName = mUserNmaeEt.getText().toString();
		final String userPassword = mUserPasswordEt.getText().toString();
		String confirmPasd = mUserPasswordConfirm.getText().toString();
		if(TextUtils.isEmpty(userEmail)){
			Toast.makeText(mContext, "邮箱不能为空",Toast.LENGTH_SHORT).show();
		}
		if(TextUtils.isEmpty(userName)){
			Toast.makeText(mContext, "用户名",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(userPassword)){
			Toast.makeText(mContext, "密码不能为空",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(confirmPasd)){
			Toast.makeText(mContext, "请确认密码",Toast.LENGTH_SHORT).show();
			return;
		}
		if(!userPassword.equals(confirmPasd)){
			Toast.makeText(mContext, "两次密码输入不一致",Toast.LENGTH_SHORT).show();
			return;
		}
    	//用户名字就是微博用户昵称
    	avUser.setUsername(userName);
    	//用户密码就是微博账号的uid
    	avUser.setPassword(userPassword);
    	//这里用户id也是uid和密码一样
    	avUser.put("uid", NoteUtil.getId());
    	//用户头像url，此url是微博头像的url,此图片用于展示给用户自己看
        avUser.signUpInBackground(new SignUpCallback() {
    	    public void done(AVException e) {
    	        if (e == null) {
    	        	//注册成功就登陆
    	        	AVUser.logInInBackground(userName, userPassword, new LogInCallback() {
    	        	    public void done(AVUser user, AVException e) {
    	        	        if (user != null) {
    	        	            // 登录成功跳转到主界面
    	        	        	Intent intent = new Intent();
    	        		        intent.setClass(mContext, MainActivity.class);
    	        		        mProgressLogin.dismiss();
    	        		        startActivity(intent);
    	        		        finish();
    	        	        } else {
    	        	        	mProgressLogin.dismiss();
    	        	            Toast.makeText(mContext, "授权成功，登陆失败！请检查网络状态！", Toast.LENGTH_SHORT).show();
    	        	        }
    	        	    }
    	        	});
    	        } else {
    	            //注册失败
    	        	mProgressLogin.dismiss();
    	        	Toast.makeText(mContext, "注册失败，请检查网络状态！", Toast.LENGTH_SHORT).show();
    	        }
    	    }
    	});
	}
	/**
	 * 初始化监听器
	 */
   private void initListener(){
	   ClickListener listener = new ClickListener();
	   mLoginBtn.setOnClickListener(listener);
	   mCancelBtn.setOnClickListener(listener);
   }
   /**
    * 单击事件监听
    * @author:  liwei
    * @Description:  TODO 
    * @date:  2015年4月16日
    */
	private class ClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			 switch (v.getId()) {
			case R.id.regsiter_btn:
				readInput();
				break;
			case R.id.cancel_reg:
				finish();
				break;
			default:
				break;
			}
		}
		
	}
}
