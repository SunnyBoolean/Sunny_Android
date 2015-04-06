/**
 * 
 */
package com.scho.note.ui.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.note.ui.activity.R;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.scho.note.basic.activity.BaseMainUIActivity;
import com.scho.note.basic.activity.BaseSecondActivity;
import com.scho.note.client.NoteCommentInfoClient;
import com.scho.note.client.NoteInfoClient;
import com.scho.note.entity.NoteCommentInfo;
import com.scho.note.entity.NoteInfo;

/**
 * @author 李伟
 * @Description: 笔记详情，用户在首页单机某条笔记列表会跳转到此界面来，此界面展示笔记详情以及所有评论
 * @date:2015年4月4日 下午8:55:51
 */

public class NoteInfoDetails extends BaseSecondActivity{
	
	/** 用户头像*/
	private ImageView mUserImage;
	/** 用户昵称*/
	private TextView mUserScreenName;
	/** 发表时间*/
	private TextView mNoteCreateTime;
	/** 笔记文本内容*/
	private EmojiconTextView mNoteContent;
	/** 笔记内容图片*/
	private ImageView mNoteContentImage;
	/** 转发*/
	private TextView mTakeSend;
	/** 评论*/
	private TextView mComment;
	/** 点赞*/
	private TextView mZan;
	/** 获取笔记内容图像参数*/
	private DisplayImageOptions mDisplayNoteContent;
	/** 获取评论列表中用户头像参数*/
	private DisplayImageOptions mDisplayCommentUserImage;
	/** 获取当前笔记用户的头像*/
	private DisplayImageOptions mDisplayNoteUserImage;
	/** 评论列表ListView*/
	private ListView mNoteCommentListView;
	/** 评论列表适配器*/
	private CommentListAdapter mNoteCommentListAdapter;
	/** 评论数据源*/
	private ArrayList<NoteCommentInfo> mNoteCommentDataList = new ArrayList<NoteCommentInfo>();
	/** 当前笔记*/
	private NoteInfo mCurNoteInfo;
	/** 主题详情与评论列表的分割器*/
	private TextView mDevier;
	
	
	@Override
	public void initActionBar() {
		super.initActionBar();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initCompontent() {
		super.initCompontent();
		
		setContentView(R.layout.layout_noteinfo_detail);
		View noteDetailV = LayoutInflater.from(mContext).inflate(R.layout.note_detail_listhead, null);
		mUserImage = (ImageView) noteDetailV.findViewById(R.id.note_detail_userimage);
		mUserScreenName = (TextView) noteDetailV.findViewById(R.id.note_detail_username);
		mNoteCreateTime = (TextView) noteDetailV.findViewById(R.id.note_detail_noteTime);
		mNoteContent = (EmojiconTextView) noteDetailV.findViewById(R.id.note_detail_notecontent);
		mNoteContentImage = (ImageView) noteDetailV.findViewById(R.id.note_detail_contentimage);
		mDevier = (TextView)noteDetailV.findViewById(R.id.note_comment_diver);
		
		mComment = (TextView) findViewById(R.id.note_detail_comment);
		mTakeSend = (TextView) findViewById(R.id.note_detail_takeover);
		mZan = (TextView) findViewById(R.id.note_detail_zan);
		
		mNoteCommentListView = (ListView) findViewById(R.id.note_detail_notecomment);
		mCurNoteInfo = (NoteInfo) getIntent().getSerializableExtra("noteInfo");
		//初始化笔记内容图片参数
		mDisplayNoteContent = new DisplayImageOptions.Builder().cacheOnDisc()
						.cacheInMemory().showStubImage(R.drawable.placeholder_image)
						.showImageOnFail(R.drawable.placeholder_image)
						.showImageForEmptyUri(R.drawable.placeholder_image).build();
		//初始化获取发表用户的头像参数
		mDisplayCommentUserImage = new DisplayImageOptions.Builder().cacheOnDisc()
						.cacheInMemory().showStubImage(R.drawable.use_pic)
						.showImageOnFail(R.drawable.use_pic)
						.showImageForEmptyUri(R.drawable.use_pic).build();
		//初始化当前笔记用户头像参数
		mDisplayNoteUserImage = new DisplayImageOptions.Builder().cacheOnDisc()
				.cacheInMemory().showStubImage(R.drawable.use_pic)
				.showImageOnFail(R.drawable.use_pic)
				.showImageForEmptyUri(R.drawable.use_pic).build();
		//添加ListView头部信息
		mNoteCommentListView.addHeaderView(noteDetailV);
		//数据初始化
		initCommentData();
		//创建评论列表适配器
		
		//设置适配器
//		mNoteCommentListView.setAdapter(mNoteCommentListAdapter);
		
	}
	/**
	 * 初始化评论列表
	 * Description: TODO
	 * @author: 李伟
	 * @date:2015年4月4日 下午10:16:45
	 */
    public void initCommentData(){
    	
    	//笔记相关数据初始化
    	if(mCurNoteInfo !=null){
    		//设置当前笔记用户头像
    		ImageLoader.getInstance().displayImage(mCurNoteInfo.userImageUrl,mUserImage,mDisplayNoteUserImage);
    		//设置当前笔记用户昵称
    		mUserScreenName.setText(mCurNoteInfo.userName);
    		//笔记发表时间
    		mNoteCreateTime.setText(mCurNoteInfo.noteCreateTime);
    		//笔记文本内容
    		mNoteContent.setText(mCurNoteInfo.noteContent);
    		//设置笔记内容图片
    		ImageLoader.getInstance().displayImage(mCurNoteInfo.noteImageUrl, mNoteContentImage,mDisplayNoteContent);
    		
    		//获取评论信息
    		new NoteCommentLoadAnsyTask().execute(mCurNoteInfo.noteId, mCurNoteInfo.userId);
    		//评论列表相关数据
//    		mNoteCommentDataList = NoteCommentInfoClient.getNoteCommentList(mCurNoteInfo.noteId, mCurNoteInfo.userId);
    	}
    	
    }
    private class NoteCommentLoadAnsyTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// 数据初始化
//			loadNoteData(Integer.parseInt(params[0]));
			AVUser currentUser = AVUser.getCurrentUser();
			if(currentUser != null){
				mNoteCommentDataList = NoteCommentInfoClient.getNoteCommentList(params[0],params[1]);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mNoteCommentListAdapter = new CommentListAdapter(mContext,mNoteCommentDataList);
			mNoteCommentListView.setAdapter(mNoteCommentListAdapter);
			
			//在此处作一个小逻辑处理，为了界面稍微好看点，如果有评论就将此分隔条展示出来，否则。默认是隐藏的
			if(mNoteCommentDataList.size() != 0){
				mDevier.setVisibility(View.VISIBLE);
			}
//			mNoteCommentListAdapter.notifyDataSetChanged();
		}

	}
	@Override
	public void initListener() {
		super.initListener();
		ClickListener listener = new ClickListener();
		mTakeSend.setOnClickListener(listener);
		mComment.setOnClickListener(listener);
		mZan.setOnClickListener(listener);
	}
    /**
     * 笔记评论列表数据适配器
     * @author 李伟
     * @Description:TODO 
     * @date:2015年4月4日 下午11:24:51
     */
	private class CommentListAdapter extends BaseAdapter{
        //数据源
		ArrayList<NoteCommentInfo> objects;
		//填充其
		LayoutInflater inflater;
		
		public CommentListAdapter(Context context,ArrayList<NoteCommentInfo> objects){
			this.objects = objects;
			inflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			if( objects !=null){
				return objects.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
             if(objects != null){
            	 return objects.get(position);
             }
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.note_comment_listitem,null);
                holder = new ViewHolder();	
                holder.userImage = (ImageView) convertView.findViewById(R.id.note_comment_userimage);
                holder.userName = (TextView) convertView.findViewById(R.id.note_comment_username);
                holder.commentTime = (TextView) convertView.findViewById(R.id.note_comment_comment_time);
               holder.commentContent = (TextView)convertView.findViewById(R.id.note_comment_content);
               
                convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			NoteCommentInfo noteCom = objects.get(position);
			if(noteCom !=null && !TextUtils.isEmpty(noteCom.userImageUrl)){
				ImageLoader.getInstance().displayImage(noteCom.userImageUrl, holder.userImage,mDisplayCommentUserImage);
			}
			holder.userName.setText(noteCom.userName);
			holder.commentTime.setText(noteCom.ntCommentTime);
			holder.commentContent.setText(noteCom.noContent);
			
			return convertView;
		}
		
		class ViewHolder{
			//评论用户的头像
			ImageView userImage;
			//评论用户的昵称
			TextView userName;
			//评论发表的时间
			TextView commentTime;
			//评论内容
			TextView commentContent;
		}
	}
	
	/**
	 * 控件单击事件
	 * @author 李伟
	 * @Description:TODO 
	 * @date:2015年4月5日 下午2:55:27
	 */
	private class ClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
              switch (v.getId()) {
              //点击进行评论
			case R.id.note_detail_comment:
				Intent intent = new Intent();
				intent.setClass(mContext, NoteCommentActivity.class);
				intent.putExtra("note", mCurNoteInfo);
				startActivityForResult(intent, 0);
				break;
				//点击转发
			case R.id.note_detail_takeover:
				
				break;
				//点赞
			case R.id.note_detail_zan:
				
				break;
			default:
				break;
			}			
		}
		
	}
	@Override
	public void onActivityResult(int requestCode,int responseCode,Intent data){
		super.onActivityResult(requestCode, responseCode, data);
		if(requestCode == 0){
			if(data !=null){
				//如果上传成功就刷新评论
				if(data.getExtras().getInt("isOk") == 1){
					new NoteCommentLoadAnsyTask().execute(mCurNoteInfo.noteId, mCurNoteInfo.userId); 
				}
			}
		}
	}
}
