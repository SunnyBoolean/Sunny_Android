/**
 * 
 */
package com.scho.ui.activity;

import com.scho.ui.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author: liwei
 * @Description: 注册界面
 * @date: 2015年4月16日
 */
public class RegisterActivity extends Activity {
	/** 邮箱*/
	private EditText mUserEmialEt;
	/** 用户名 */
	private EditText mUserNmaeEt;
	/** 用户密码 */
	private EditText mUserPasswordEt;
	/** 登录 */
	private Button mLoginBtn;
	/** 注册 */
	private Button mCancelBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_register);
		super.onCreate(savedInstanceState);
		mUserEmialEt = (EditText) findViewById(R.id.user_email);
	    mUserNmaeEt = (EditText) findViewById(R.id.user_name);
	    mUserPasswordEt = (EditText) findViewById(R.id.user_password);
	    mLoginBtn = (Button) findViewById(R.id.regsiter_btn);
	    mCancelBtn = (Button) findViewById(R.id.cancel_btn);
		initListener();
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
    * @author:  hello
    * @Description:  TODO 
    * @date:  2015年4月16日
    */
	private class ClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			 switch (v.getId()) {
			case R.id.regsiter_btn:
				
				break;
			case R.id.cancel_btn:
				
				break;
			default:
				break;
			}
		}
		
	}
}
