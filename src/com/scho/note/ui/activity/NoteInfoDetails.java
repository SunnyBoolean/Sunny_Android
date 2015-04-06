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
 * @author ��ΰ
 * @Description: �ʼ����飬�û�����ҳ����ĳ���ʼ��б����ת���˽��������˽���չʾ�ʼ������Լ���������
 * @date:2015��4��4�� ����8:55:51
 */

public class NoteInfoDetails extends BaseSecondActivity{
	
	/** �û�ͷ��*/
	private ImageView mUserImage;
	/** �û��ǳ�*/
	private TextView mUserScreenName;
	/** ����ʱ��*/
	private TextView mNoteCreateTime;
	/** �ʼ��ı�����*/
	private EmojiconTextView mNoteContent;
	/** �ʼ�����ͼƬ*/
	private ImageView mNoteContentImage;
	/** ת��*/
	private TextView mTakeSend;
	/** ����*/
	private TextView mComment;
	/** ����*/
	private TextView mZan;
	/** ��ȡ�ʼ�����ͼ�����*/
	private DisplayImageOptions mDisplayNoteContent;
	/** ��ȡ�����б����û�ͷ�����*/
	private DisplayImageOptions mDisplayCommentUserImage;
	/** ��ȡ��ǰ�ʼ��û���ͷ��*/
	private DisplayImageOptions mDisplayNoteUserImage;
	/** �����б�ListView*/
	private ListView mNoteCommentListView;
	/** �����б�������*/
	private CommentListAdapter mNoteCommentListAdapter;
	/** ��������Դ*/
	private ArrayList<NoteCommentInfo> mNoteCommentDataList = new ArrayList<NoteCommentInfo>();
	/** ��ǰ�ʼ�*/
	private NoteInfo mCurNoteInfo;
	/** ���������������б�ķָ���*/
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
		//��ʼ���ʼ�����ͼƬ����
		mDisplayNoteContent = new DisplayImageOptions.Builder().cacheOnDisc()
						.cacheInMemory().showStubImage(R.drawable.placeholder_image)
						.showImageOnFail(R.drawable.placeholder_image)
						.showImageForEmptyUri(R.drawable.placeholder_image).build();
		//��ʼ����ȡ�����û���ͷ�����
		mDisplayCommentUserImage = new DisplayImageOptions.Builder().cacheOnDisc()
						.cacheInMemory().showStubImage(R.drawable.use_pic)
						.showImageOnFail(R.drawable.use_pic)
						.showImageForEmptyUri(R.drawable.use_pic).build();
		//��ʼ����ǰ�ʼ��û�ͷ�����
		mDisplayNoteUserImage = new DisplayImageOptions.Builder().cacheOnDisc()
				.cacheInMemory().showStubImage(R.drawable.use_pic)
				.showImageOnFail(R.drawable.use_pic)
				.showImageForEmptyUri(R.drawable.use_pic).build();
		//���ListViewͷ����Ϣ
		mNoteCommentListView.addHeaderView(noteDetailV);
		//���ݳ�ʼ��
		initCommentData();
		//���������б�������
		
		//����������
//		mNoteCommentListView.setAdapter(mNoteCommentListAdapter);
		
	}
	/**
	 * ��ʼ�������б�
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��4��4�� ����10:16:45
	 */
    public void initCommentData(){
    	
    	//�ʼ�������ݳ�ʼ��
    	if(mCurNoteInfo !=null){
    		//���õ�ǰ�ʼ��û�ͷ��
    		ImageLoader.getInstance().displayImage(mCurNoteInfo.userImageUrl,mUserImage,mDisplayNoteUserImage);
    		//���õ�ǰ�ʼ��û��ǳ�
    		mUserScreenName.setText(mCurNoteInfo.userName);
    		//�ʼǷ���ʱ��
    		mNoteCreateTime.setText(mCurNoteInfo.noteCreateTime);
    		//�ʼ��ı�����
    		mNoteContent.setText(mCurNoteInfo.noteContent);
    		//���ñʼ�����ͼƬ
    		ImageLoader.getInstance().displayImage(mCurNoteInfo.noteImageUrl, mNoteContentImage,mDisplayNoteContent);
    		
    		//��ȡ������Ϣ
    		new NoteCommentLoadAnsyTask().execute(mCurNoteInfo.noteId, mCurNoteInfo.userId);
    		//�����б��������
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
			// ���ݳ�ʼ��
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
			
			//�ڴ˴���һ��С�߼�����Ϊ�˽�����΢�ÿ��㣬��������۾ͽ��˷ָ���չʾ����������Ĭ�������ص�
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
     * �ʼ������б�����������
     * @author ��ΰ
     * @Description:TODO 
     * @date:2015��4��4�� ����11:24:51
     */
	private class CommentListAdapter extends BaseAdapter{
        //����Դ
		ArrayList<NoteCommentInfo> objects;
		//�����
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
			//�����û���ͷ��
			ImageView userImage;
			//�����û����ǳ�
			TextView userName;
			//���۷����ʱ��
			TextView commentTime;
			//��������
			TextView commentContent;
		}
	}
	
	/**
	 * �ؼ������¼�
	 * @author ��ΰ
	 * @Description:TODO 
	 * @date:2015��4��5�� ����2:55:27
	 */
	private class ClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
              switch (v.getId()) {
              //�����������
			case R.id.note_detail_comment:
				Intent intent = new Intent();
				intent.setClass(mContext, NoteCommentActivity.class);
				intent.putExtra("note", mCurNoteInfo);
				startActivityForResult(intent, 0);
				break;
				//���ת��
			case R.id.note_detail_takeover:
				
				break;
				//����
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
				//����ϴ��ɹ���ˢ������
				if(data.getExtras().getInt("isOk") == 1){
					new NoteCommentLoadAnsyTask().execute(mCurNoteInfo.noteId, mCurNoteInfo.userId); 
				}
			}
		}
	}
}
