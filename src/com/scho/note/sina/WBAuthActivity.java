/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scho.note.sina;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.note.ui.activity.R;
import com.scho.note.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * SSO授权界面
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBAuthActivity extends Activity {
    
    private static final String TAG = "weibosdk";

    private TextView mTokenText;
    /** 用户信息*/
    private AuthInfo mAuthInfo;
    
    private Oauth2AccessToken mAccessToken;

    private SsoHandler mSsoHandler;

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        
        mTokenText = (TextView) findViewById(R.id.token_text_view);
        TextView hintView = (TextView) findViewById(R.id.obtain_token_hint);
        hintView.setMovementMethod(new ScrollingMovementMethod());

        mAuthInfo = new AuthInfo(this, Constants.Sina_Code.APP_KEY, Constants.Sina_Code.REDIRECT_URL, Constants.Sina_Code.SCOPE);
        mSsoHandler = new SsoHandler(WBAuthActivity.this, mAuthInfo);
        
        findViewById(R.id.obtain_token_via_sso).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorizeClientSso(new AuthListener());
            }
        });

        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
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
     */
    class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                updateTokenView(false);
                
                AccessTokenKeeper.writeAccessToken(WBAuthActivity.this, mAccessToken);
                Toast.makeText(WBAuthActivity.this, 
                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
            } else {
                String code = values.getString("code");
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(WBAuthActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(WBAuthActivity.this, 
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(WBAuthActivity.this, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * 
     * @param hasExisted
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));
        
        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        mTokenText.setText(message);
    }
}