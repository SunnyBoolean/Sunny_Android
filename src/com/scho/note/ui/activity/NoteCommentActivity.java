/**
 * 
 */
package com.scho.note.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.note.ui.activity.R;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.scho.note.basic.activity.BaseMainUIActivity;
import com.scho.note.client.HttpResultInfo;
import com.scho.note.client.NoteCommentInfoClient;
import com.scho.note.entity.NoteCommentInfo;
import com.scho.note.entity.NoteInfo;

/**
 * @author ��ΰ
 * @Description:TODO 
 * @date:2015��4��5�� ����3:21:51
 */

public class NoteCommentActivity extends BaseMainUIActivity{
	/** ȡ��*/
	private TextView mCancelTv;
	/** ȷ��*/
	private TextView mConfirmTv;
	/** ��������*/
	private EmojiconEditText mNoteCommentContent;
	/** NoteInfo��Ϣ*/
	private NoteInfo mCurNoteInfo;
	/** NoteCommentInfo��Ϣ*/
	private NoteCommentInfo mNoteCommentInfo;
	
   /** ActionBar*/
	@Override
	public void initActionBar(){
		super.initActionBar();
		mActionBar.setIcon(R.drawable.ic_actionbar_back);
		mActionBar.setTitle("��������");
	}
	@Override
	public void initCompontent(){
		setContentView(R.layout.layout_note_comment);
		mCurNoteInfo = (NoteInfo) getIntent().getSerializableExtra("note");
		mCancelTv = (TextView) findViewById(R.id.note_comment_cancel);
		mConfirmTv = (TextView) findViewById(R.id.note_comment_confirm);
		mNoteCommentContent = (EmojiconEditText) findViewById(R.id.note_coment_content);
		super.initCompontent();
	}
	@Override
	public void initListener(){
		ClilckListener listener = new ClilckListener();
		mCancelTv.setOnClickListener(listener);
		mConfirmTv.setOnClickListener(listener);
		
	}
	/**
	 * ��ȡ�û�����
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��4��5�� ����5:20:10
	 */
	public void readInput(){
		if(mNoteCommentInfo == null){
			mNoteCommentInfo = new NoteCommentInfo();
		}
		//��ȡ��������
		String content = mNoteCommentContent.getText().toString();
		if(mCurNoteInfo !=null && !TextUtils.isEmpty(content)){
			mNoteCommentInfo.noContent = content;
			mNoteCommentInfo.noteId = mCurNoteInfo.noteId;
			mNoteCommentInfo.userId = mCurNoteInfo.userId;
			mNoteCommentInfo.userImageUrl = mCurNoteInfo.userImageUrl;
			mNoteCommentInfo.userName = mCurNoteInfo.userName;
		}else{
			Toast.makeText(mContext, "�������ݲ���Ϊ��",Toast.LENGTH_SHORT).show();
			
			return;
		}
		new UploadCommentTask().execute(mNoteCommentInfo);
	}
	/**
	 * �ϴ������첽��
	 * @author ��ΰ
	 * @Description:TODO 
	 * @date:2015��4��5�� ����5:53:12
	 */
	private class UploadCommentTask extends AsyncTask<NoteCommentInfo, Void,HttpResultInfo>{
       
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(mProgress == null){
				mProgress = new ProgressDialog(mContext);
			}
			mProgress.setMessage("����������..");
			mProgress.show();
		}
		@Override
		protected HttpResultInfo doInBackground(NoteCommentInfo... params) {
			HttpResultInfo resultInfo = NoteCommentInfoClient.uploadComment(params[0]);
			return resultInfo;
		}
		@Override
		protected void onPostExecute(HttpResultInfo result) {
			super.onPostExecute(result);
			if(mProgress !=null){
				mProgress.dismiss();
			}
			if(!result.success){
				Toast.makeText(mContext, result.resultInfo, Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent();
				intent.putExtra("isOk", 1);
				setResult(0, intent);
			}
			finish();
		}
	}
	/**
	 * �����¼�
	 * @author ��ΰ
	 * @Description:TODO 
	 * @date:2015��4��5�� ����6:20:39
	 */
	private class ClilckListener implements OnClickListener{

		public void onClick(View v) {
            switch (v.getId()) {
			case R.id.note_comment_confirm:
				readInput();
				break;
			case R.id.note_comment_cancel:
				finish();
			default:
				break;
			}			
		}
		
	}
}
