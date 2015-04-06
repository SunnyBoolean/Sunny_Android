/**
 * 
 */
package com.scho.note.ui.activity;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.note.ui.activity.R;
import com.scho.note.Constants;
import com.scho.note.basic.activity.BaseMainUIActivity;
import com.scho.note.sina.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * @author ��ΰ
 * @Description:��½���棬��������ṩ����΢����Ȩ��½��QQ��Ȩ��½
 * @date:2015��4��2�� ����9:22:45
 */

public class LoginActivity extends BaseMainUIActivity{
	
	/** ����΢����Ȩ��½��ť*/
	private Button mSinaLoginBtn;
	  /** �û���Ϣ*/
    private AuthInfo mAuthInfo;
    /** oauth��֤*/
    private Oauth2AccessToken mAccessToken;
    /** ������������Ȩ�첽����*/
    private SsoHandler mSsoHandler;
    /** �����û���Ȩ�ɹ������Ϣ*/
    private ContentValues mUserInfoCV = new ContentValues();
    /** �û���Ϣ�ӿ� */
    private UsersAPI mUsersAPI;
    /** ��ȡ�����˺���Ϣ�Ļص��ӿ�*/
    private SinaRequestListener mSinaListener;
    /** ��½������*/
    private ProgressDialog mProgressLogin;
    
	  @Override
	    public void initCompontent(){
	    	//����һ��Handlerʵ��
	    	setContentView(R.layout.layout_login_main);
	    	mSinaLoginBtn = (Button) findViewById(R.id.sina_login);

	        mAuthInfo = new AuthInfo(this, Constants.Sina_Code.APP_KEY, Constants.Sina_Code.REDIRECT_URL, Constants.Sina_Code.SCOPE);
	        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
	        mAccessToken = AccessTokenKeeper.readAccessToken(this);
	        mSinaListener = new SinaRequestListener();
	    
	        if (mAccessToken.isSessionValid()) {
	            updateTokenView(true);
	        }
	    }
	  
	  @Override
	  public void initListener(){
		  ClickListener listener = new ClickListener();
		  mSinaLoginBtn.setOnClickListener(listener);
	  }
	  /**
	   * �����¼�
	   * @author ��ΰ
	   * @Description:TODO 
	   * @date:2015��4��2�� ����10:33:19
	   */
	  private class ClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
                switch (v.getId()) {
				case R.id.sina_login:
					mSsoHandler.authorizeClientSso(new AuthListener());
					break;

				default:
					break;
				}			
		}
		  
	  }
	  
      /**
       * 
       * @author ��ΰ
       * @Description:TODO 
       * @date:2015��4��2�� ����10:33:41
       */
	  class AuthListener implements WeiboAuthListener {
	        
	        @Override
	        public void onComplete(Bundle values) {
	            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
	            if (mAccessToken.isSessionValid()) {
	            	//��ʾ�ǵ�һ��ʹ�ã����Ҿ�����Ȩ��
	                updateTokenView(false);
	                AccessTokenKeeper.writeAccessToken(mContext, mAccessToken);
	                Toast.makeText(mContext, 
	                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
	            } else {
	                String code = values.getString("code");
	                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
	                if (!TextUtils.isEmpty(code)) {
	                    message = message + "\nObtained the code: " + code;
	                }
	                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	            }
	        }

	        @Override
	        public void onCancel() {
	            Toast.makeText(mContext, 
	                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
	        }

	        @Override
	        public void onWeiboException(WeiboException e) {
	            Toast.makeText(mContext, 
	                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
	        }
	    }
	  
	    /**
	     * 
	     * @see {@link Activity#onActivityResult}
	     */
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        
	        if (mSsoHandler != null) {
	            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	        }
	    }

	    /**
	     * ��Ȩ�ɹ�
	     * @param hasExisted
	     */
	    private void updateTokenView(boolean hasExisted) {
	        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
	                new java.util.Date(mAccessToken.getExpiresTime()));
	        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
	        String message = String.format(format, mAccessToken.getToken(), date);
	        //����Ϣ�浽ContentValues�У���������õ�
	        mUserInfoCV.put(Constants.UserInfo.SINA_ACCESS_TOKEN, mAccessToken.getToken());
        	mUserInfoCV.put(Constants.UserInfo.SINA_APP_KEY, Constants.Sina_Code.APP_KEY);
        	mUserInfoCV.put(Constants.UserInfo.SINA_USER_ID, mAccessToken.getUid());
        	
	        if (hasExisted) {
	            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
	        //������ǵ�һ����Ȩע���½���Ժ�ÿ�ν����Ͷ�ֱ�ӽ���������
	         // ��¼�ɹ���ת��������
	        	Intent intent = new Intent();
		        intent.setClass(mContext, MainActivity.class);
		        intent.putExtra("sina_user", mUserInfoCV);
		        startActivity(intent);
		        finish();
	        }else{
	        	//��Ȩ�ɹ������Ȼ�ȡ�û���Ϣ��Ȼ���û���Ϣ�ϴ����������˱��棬�����һ�ε�½������½�ɹ�����ת���������ʾ��½ʧ��
	        	//�ӵ��أ���UserAPI���У�������show�������������ƣ��ڶ����������ǻص��ӿڣ���һ���������������uid����ѯ��ô�ʹ���long���Ͳ���
	        	//�������û��ǳƲ��Ǵ����ַ����������������uid����ѯ֮���Ա��������Ϊû�а�Stringת��ΪLong�������˸����û��ǳ�����ѯ�Ľӿڣ���Ȼ�û��������ˣ�
	        	//ֻ�ڵ�һ����Ȩʱ����ע�ᡢ��½
	        	mProgressLogin = new ProgressDialog(mContext);
	        	mProgressLogin.setMessage("���ڵ�½...");
	        	mProgressLogin.show();
	        	mUsersAPI = new UsersAPI(this, Constants.Sina_Code.APP_KEY, mAccessToken);
	        	mUsersAPI.show(Long.parseLong(mAccessToken.getUid()), mSinaListener);
	        }
	    }
	    /**
		 * ΢��OpenAPI�ص��ӿ�
		 * @author ��ΰ
		 * @Description:TODO 
		 * @date:2015��4��3�� ����5:39:53
		 */
	    private class SinaRequestListener implements RequestListener{
	    	 @Override
	         public void onComplete(String response) {
	             if (!TextUtils.isEmpty(response)) {
	                 // ���� User#parse ��JSON��������User����
	                 User user = User.parse(response);
	                 if (user != null) {
	                	 //��ȡ�ɹ�,��ʼ���û���Ϣ�ϴ�����������棨��ע�Ṧ�ܣ��Զ�ע�ᣬ������Զ���½��
	                	 uploadUserInfoServer(user);
	                 } else {
	                     Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
	                 }
	             }
	         }
	         @Override
	         public void onWeiboException(WeiboException e) {
	             ErrorInfo info = ErrorInfo.parse(e.getMessage());
	             Toast.makeText(mContext, info.toString(), Toast.LENGTH_LONG).show();
	         }
	    }
	    /**
	     * ���û���Ȩ����½�ɹ����������������û�������Ϣ
	     */
	    private void uploadUserInfoServer(final User user){
	    	AVUser avUser = new AVUser();
	    	//�û����־���΢���û��ǳ�
	    	avUser.setUsername(user.screen_name);
	    	//�û��������΢���˺ŵ�uid
	    	avUser.setPassword(mAccessToken.getUid());
	    	//�����û�idҲ��uid������һ��
	    	avUser.put("uid", mAccessToken.getUid());
	    	//�û�ͷ��url����url��΢��ͷ���url,��ͼƬ����չʾ���û��Լ���
	        avUser.put("userImageUrl",user.avatar_large );
	        //����ҳչʾ�ļ�¼����ʾ�����¼�û���ͼ��50*50
	        avUser.put("noteUserUrl", user.profile_image_url);
	        //�Ա�m��ʾ�У�f��ʾŮ��n��ʾδ֪
	        avUser.put("userSex", user.gender);
	        //�û����ڵ�
	        avUser.signUpInBackground(new SignUpCallback() {
	    	    public void done(AVException e) {
	    	        if (e == null) {
	    	        	//ע��ɹ��͵�½
	    	        	AVUser.logInInBackground(user.screen_name, mAccessToken.getUid(), new LogInCallback() {
	    	        	    public void done(AVUser user, AVException e) {
	    	        	        if (user != null) {
	    	        	            // ��¼�ɹ���ת��������
	    	        	        	Intent intent = new Intent();
	    	        		        intent.setClass(mContext, MainActivity.class);
	    	        		        intent.putExtra("sina_user", mUserInfoCV);
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
}
