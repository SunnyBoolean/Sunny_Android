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
 * @Description: ע�����
 * @date: 2015��4��16��
 */
public class RegisterActivity extends Activity {
	/** ����*/
	private EditText mUserEmialEt;
	/** �û��� */
	private EditText mUserNmaeEt;
	/** �û����� */
	private EditText mUserPasswordEt;
	/** ��¼ */
	private Button mLoginBtn;
	/** ע�� */
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
	 * ��ʼ��������
	 */
   private void initListener(){
	   ClickListener listener = new ClickListener();
	   mLoginBtn.setOnClickListener(listener);
	   mCancelBtn.setOnClickListener(listener);
   }
   /**
    * �����¼�����
    * @author:  hello
    * @Description:  TODO 
    * @date:  2015��4��16��
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
