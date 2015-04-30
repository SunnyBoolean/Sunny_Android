/**
 * 
 */
package com.scho.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scho.client.SchoolInfoClient;
import com.scho.entity.MemoInfo;
import com.scho.ui.R;

/**
 * @author: liwei
 * @Description:  �½�����¼ 
 * @date:  2015��4��22��
 */
public class AddNewMemoActivity extends Activity{
    /** ������*/
	private Context mContext;
	/** ����*/
	private EditText mContentEt;
	/** ���*/
	private Button mCompleteBt;
	/** �½�����*/
	private MemoInfo mMemoInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_memo);
		mContext = this;
		mContentEt = (EditText) findViewById(R.id.new_memo_content);
		mCompleteBt = (Button) findViewById(R.id.memo_complete);
		mCompleteBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(readInput()){
					SchoolInfoClient.uploadNewMemo(mMemoInfo, mContext);
					setResult(15);
					finish();
				}else{
					Toast.makeText(mContext, "���ݲ���Ϊ��", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private boolean readInput(){
		if(mMemoInfo ==null){
			mMemoInfo = new MemoInfo();
		}
		String content = mContentEt.getText().toString();
		if(TextUtils.isEmpty(content)){
			return false;
		}else{
			mMemoInfo.content = content;
		}
          return true;		
	}
	
  
}
