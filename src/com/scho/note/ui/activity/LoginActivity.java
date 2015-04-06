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
 * @author 李伟
 * @Description:登陆界面，这个界面提供新浪微博授权登陆和QQ授权登陆
 * @date:2015年4月2日 下午9:22:45
 */

public class LoginActivity extends BaseMainUIActivity{
	
	/** 新浪微博授权登陆按钮*/
	private Button mSinaLoginBtn;
	  /** 用户信息*/
    private AuthInfo mAuthInfo;
    /** oauth认证*/
    private Oauth2AccessToken mAccessToken;
    /** 服务器请求授权异步处理*/
    private SsoHandler mSsoHandler;
    /** 保存用户授权成功后的信息*/
    private ContentValues mUserInfoCV = new ContentValues();
    /** 用户信息接口 */
    private UsersAPI mUsersAPI;
    /** 获取新浪账号信息的回调接口*/
    private SinaRequestListener mSinaListener;
    /** 登陆进度条*/
    private ProgressDialog mProgressLogin;
    
	  @Override
	    public void initCompontent(){
	    	//创建一个Handler实例
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
	   * 单击事件
	   * @author 李伟
	   * @Description:TODO 
	   * @date:2015年4月2日 下午10:33:19
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
       * @author 李伟
       * @Description:TODO 
       * @date:2015年4月2日 下午10:33:41
       */
	  class AuthListener implements WeiboAuthListener {
	        
	        @Override
	        public void onComplete(Bundle values) {
	            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
	            if (mAccessToken.isSessionValid()) {
	            	//表示是第一次使用，并且经过授权，
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
	     * 授权成功
	     * @param hasExisted
	     */
	    private void updateTokenView(boolean hasExisted) {
	        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
	                new java.util.Date(mAccessToken.getExpiresTime()));
	        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
	        String message = String.format(format, mAccessToken.getToken(), date);
	        //讲信息存到ContentValues中，主界面会用到
	        mUserInfoCV.put(Constants.UserInfo.SINA_ACCESS_TOKEN, mAccessToken.getToken());
        	mUserInfoCV.put(Constants.UserInfo.SINA_APP_KEY, Constants.Sina_Code.APP_KEY);
        	mUserInfoCV.put(Constants.UserInfo.SINA_USER_ID, mAccessToken.getUid());
        	
	        if (hasExisted) {
	            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
	        //如果不是第一次授权注册登陆，以后每次进来就都直接进入主界面
	         // 登录成功跳转到主界面
	        	Intent intent = new Intent();
		        intent.setClass(mContext, MainActivity.class);
		        intent.putExtra("sina_user", mUserInfoCV);
		        startActivity(intent);
		        finish();
	        }else{
	        	//授权成功后首先获取用户信息，然后将用户信息上传到服务器端保存，最后作一次登陆，若登陆成功就跳转，否则就提示登陆失败
	        	//坑爹呢，在UserAPI类中，有两个show方法，参数类似，第二个参数都是回调接口，第一个参数，如果根据uid来查询那么就传入long类型参数
	        	//而根据用户昵称才是传入字符串！所以这里，根据uid来查询之所以报错就是因为没有把String转换为Long而调用了根据用户昵称来查询的接口！当然用户不存在了！
	        	//只在第一次授权时进行注册、登陆
	        	mProgressLogin = new ProgressDialog(mContext);
	        	mProgressLogin.setMessage("正在登陆...");
	        	mProgressLogin.show();
	        	mUsersAPI = new UsersAPI(this, Constants.Sina_Code.APP_KEY, mAccessToken);
	        	mUsersAPI.show(Long.parseLong(mAccessToken.getUid()), mSinaListener);
	        }
	    }
	    /**
		 * 微博OpenAPI回调接口
		 * @author 李伟
		 * @Description:TODO 
		 * @date:2015年4月3日 下午5:39:53
		 */
	    private class SinaRequestListener implements RequestListener{
	    	 @Override
	         public void onComplete(String response) {
	             if (!TextUtils.isEmpty(response)) {
	                 // 调用 User#parse 将JSON串解析成User对象
	                 User user = User.parse(response);
	                 if (user != null) {
	                	 //获取成功,开始将用户信息上传大服务器保存（即注册功能，自动注册，后面会自动登陆）
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
	     * 当用户授权并登陆成功后就向服务器保存用户基本信息
	     */
	    private void uploadUserInfoServer(final User user){
	    	AVUser avUser = new AVUser();
	    	//用户名字就是微博用户昵称
	    	avUser.setUsername(user.screen_name);
	    	//用户密码就是微博账号的uid
	    	avUser.setPassword(mAccessToken.getUid());
	    	//这里用户id也是uid和密码一样
	    	avUser.put("uid", mAccessToken.getUid());
	    	//用户头像url，此url是微博头像的url,此图片用于展示给用户自己看
	        avUser.put("userImageUrl",user.avatar_large );
	        //在首页展示的记录中显示发表记录用户的图像，50*50
	        avUser.put("noteUserUrl", user.profile_image_url);
	        //性别m表示男，f表示女，n表示未知
	        avUser.put("userSex", user.gender);
	        //用户所在地
	        avUser.signUpInBackground(new SignUpCallback() {
	    	    public void done(AVException e) {
	    	        if (e == null) {
	    	        	//注册成功就登陆
	    	        	AVUser.logInInBackground(user.screen_name, mAccessToken.getUid(), new LogInCallback() {
	    	        	    public void done(AVUser user, AVException e) {
	    	        	        if (user != null) {
	    	        	            // 登录成功跳转到主界面
	    	        	        	Intent intent = new Intent();
	    	        		        intent.setClass(mContext, MainActivity.class);
	    	        		        intent.putExtra("sina_user", mUserInfoCV);
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
}
