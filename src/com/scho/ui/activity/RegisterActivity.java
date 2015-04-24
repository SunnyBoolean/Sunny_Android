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
 * @Description: ע�����
 * @date: 2015��4��16��
 */
public class RegisterActivity extends Activity {
	/** ������*/
	private Context mContext;
	/** ����*/
	private EditText mUserEmialEt;
	/** �û��� */
	private EditText mUserNmaeEt;
	/** �û����� */
	private EditText mUserPasswordEt;
	/** ȷ������*/
	private EditText mUserPasswordConfirm;
	/** ��¼ */
	private Button mLoginBtn;
	/** ע�� */
	private Button mCancelBtn;
	/** ע�����*/
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
	    mProgressLogin.setMessage("ע����...");
		initListener();
	}
	/**
	 * ��ȡע����Ϣ
	 */
	private void readInput(){
		AVUser avUser = new AVUser();
		String userEmail = mUserEmialEt.getText().toString();
		final String userName = mUserNmaeEt.getText().toString();
		final String userPassword = mUserPasswordEt.getText().toString();
		String confirmPasd = mUserPasswordConfirm.getText().toString();
		if(TextUtils.isEmpty(userEmail)){
			Toast.makeText(mContext, "���䲻��Ϊ��",Toast.LENGTH_SHORT).show();
		}
		if(TextUtils.isEmpty(userName)){
			Toast.makeText(mContext, "�û���",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(userPassword)){
			Toast.makeText(mContext, "���벻��Ϊ��",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(confirmPasd)){
			Toast.makeText(mContext, "��ȷ������",Toast.LENGTH_SHORT).show();
			return;
		}
		if(!userPassword.equals(confirmPasd)){
			Toast.makeText(mContext, "�����������벻һ��",Toast.LENGTH_SHORT).show();
			return;
		}
    	//�û����־���΢���û��ǳ�
    	avUser.setUsername(userName);
    	//�û��������΢���˺ŵ�uid
    	avUser.setPassword(userPassword);
    	//�����û�idҲ��uid������һ��
    	avUser.put("uid", NoteUtil.getId());
    	//�û�ͷ��url����url��΢��ͷ���url,��ͼƬ����չʾ���û��Լ���
        avUser.signUpInBackground(new SignUpCallback() {
    	    public void done(AVException e) {
    	        if (e == null) {
    	        	//ע��ɹ��͵�½
    	        	AVUser.logInInBackground(userName, userPassword, new LogInCallback() {
    	        	    public void done(AVUser user, AVException e) {
    	        	        if (user != null) {
    	        	            // ��¼�ɹ���ת��������
    	        	        	Intent intent = new Intent();
    	        		        intent.setClass(mContext, MainActivity.class);
    	        		        mProgressLogin.dismiss();
    	        		        startActivity(intent);
    	        		        finish();
    	        	        } else {
    	        	        	mProgressLogin.dismiss();
    	        	            Toast.makeText(mContext, "��Ȩ�ɹ�����½ʧ�ܣ���������״̬��", Toast.LENGTH_SHORT).show();
    	        	        }
    	        	    }
    	        	});
    	        } else {
    	            //ע��ʧ��
    	        	mProgressLogin.dismiss();
    	        	Toast.makeText(mContext, "ע��ʧ�ܣ���������״̬��", Toast.LENGTH_SHORT).show();
    	        }
    	    }
    	});
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
    * @author:  liwei
    * @Description:  TODO 
    * @date:  2015��4��16��
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
