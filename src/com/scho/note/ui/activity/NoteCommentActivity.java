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
 * @author 李伟
 * @Description:TODO 
 * @date:2015年4月5日 下午3:21:51
 */

public class NoteCommentActivity extends BaseMainUIActivity{
	/** 取消*/
	private TextView mCancelTv;
	/** 确定*/
	private TextView mConfirmTv;
	/** 评论内容*/
	private EmojiconEditText mNoteCommentContent;
	/** NoteInfo信息*/
	private NoteInfo mCurNoteInfo;
	/** NoteCommentInfo信息*/
	private NoteCommentInfo mNoteCommentInfo;
	
   /** ActionBar*/
	@Override
	public void initActionBar(){
		super.initActionBar();
		mActionBar.setIcon(R.drawable.ic_actionbar_back);
		mActionBar.setTitle("主题评论");
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
	 * 读取用户评论
	 * Description: TODO
	 * @author: 李伟
	 * @date:2015年4月5日 下午5:20:10
	 */
	public void readInput(){
		if(mNoteCommentInfo == null){
			mNoteCommentInfo = new NoteCommentInfo();
		}
		//获取评论内容
		String content = mNoteCommentContent.getText().toString();
		if(mCurNoteInfo !=null && !TextUtils.isEmpty(content)){
			mNoteCommentInfo.noContent = content;
			mNoteCommentInfo.noteId = mCurNoteInfo.noteId;
			mNoteCommentInfo.userId = mCurNoteInfo.userId;
			mNoteCommentInfo.userImageUrl = mCurNoteInfo.userImageUrl;
			mNoteCommentInfo.userName = mCurNoteInfo.userName;
		}else{
			Toast.makeText(mContext, "评论内容不能为空",Toast.LENGTH_SHORT).show();
			
			return;
		}
		new UploadCommentTask().execute(mNoteCommentInfo);
	}
	/**
	 * 上传评论异步类
	 * @author 李伟
	 * @Description:TODO 
	 * @date:2015年4月5日 下午5:53:12
	 */
	private class UploadCommentTask extends AsyncTask<NoteCommentInfo, Void,HttpResultInfo>{
       
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(mProgress == null){
				mProgress = new ProgressDialog(mContext);
			}
			mProgress.setMessage("发送评论中..");
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
	 * 单击事件
	 * @author 李伟
	 * @Description:TODO 
	 * @date:2015年4月5日 下午6:20:39
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
