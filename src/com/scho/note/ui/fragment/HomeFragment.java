/**
 * 
 */
package com.scho.note.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.note.ui.activity.R;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.scho.note.Constants;
import com.scho.note.adapter.SetAdapter;
import com.scho.note.basic.fragment.BaseHomeFragment;
import com.scho.note.client.NoteInfoClient;
import com.scho.note.client.UserInfoClient;
import com.scho.note.entity.NoteInfo;
import com.scho.note.lib.internal.PLA_AdapterView;
import com.scho.note.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.scho.note.pla.view.XListView;
import com.scho.note.pla.view.XListView.IXListViewListener;
import com.scho.note.ui.activity.NoteInfoDetails;
import com.scho.note.utils.ImageTool;
import com.scho.note.utils.NoteUtil;
/**
 * @author liwei
 * @Description:������չʾ��Home��ҳFragment
 * @date:2015��2��13�� ����9:14:18
 */

public class HomeFragment extends BaseHomeFragment {
	/** չʾ�б� */
	private XListView mListView;
	/** ������ */
	private HomeDataAdapter mAdapter;
	/** ���رʼ������е�ͼƬ*/
	private DisplayImageOptions mDisplayImageOptions;
	/** ���رʼ����û���ͷ��*/
	private DisplayImageOptions mDisplayUserImage;
	/** ˢ�¼����� */
	private HomeDataListViewListener mHomeDataListViewListener;
	/** note���ڱʼǼ����� */
	private NoteLoadAnsyTask mNoteLoadAnsyTask;
	/** �ӷ��������ص�����Դ */
	private List<NoteInfo> mNoteItemLists = new ArrayList<NoteInfo>();
	/** ÿ�μ��ظ����ˢ�µ�ʱ���ȡ�������ݣ�ÿ�λ�ȡ���ݶ�����ӵ�mNoteItemLists��*/
	private ArrayList<NoteInfo> list;
	/** �����Č���*/
	private Context mContext;
	/** ��ҳ��ʾ��ҳ����Ĭ����һҳ��ÿ�μ��ظ���ͼ���һҳ*/
	private int mPage = 0;
	/** ÿ����һҳ��ʾ��ҳ��*/
	private final int mPageNumber = 5;
	/** ��ǰ�б�״̬���Ƿ�����ˢ��*/
	private boolean mIsRefreshing = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//��ʼ���ʼ�����ͼƬ����
		mDisplayImageOptions = new DisplayImageOptions.Builder().cacheOnDisc()
				.cacheInMemory().showStubImage(R.drawable.placeholder_image)
				.showImageOnFail(R.drawable.placeholder_image)
				.showImageForEmptyUri(R.drawable.placeholder_image).build();
		//��ʼ����ȡ�����û���ͷ�����
		mDisplayUserImage = new DisplayImageOptions.Builder().cacheOnDisc()
				.cacheInMemory().showStubImage(R.drawable.use_pic)
				.showImageOnFail(R.drawable.use_pic)
				.showImageForEmptyUri(R.drawable.use_pic).build();
	}
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_home_fragment_xlist, null);
		mListView = (XListView) v.findViewById(R.id.listview);
		mHomeDataListViewListener = new HomeDataListViewListener();
		//ListView����ˢ�µȼ���
		mListView.setXListViewListener(mHomeDataListViewListener);
		View va = inflater.inflate(R.layout.layout_home_time_comp, null);
		mListView.addHeaderView(va,null,false);
		mAdapter = new HomeDataAdapter(mContext);
		mListView.setAdapter(mAdapter);
		//Ĭ��Ϊ��������ˢ��
		mListView.setPullLoadEnable(false);
		//�б����¼�
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = null;
				NoteInfo issue = (NoteInfo) mListView.getAdapter().getItem(position);
				if (issue == null)
					return;
				if (intent != null) {
					startActivity(intent);
				}

			}
		});
		//��һ�ν���ʱ���Ƚ���ˢ��
		mListView.startRefresh(true);
		//�����¼�������ʼ��
		initListener();
		return v;
	}
	/**
     * ��������ʼ��
     * Description: TODO
     * @author: ��ΰ
     * @date:2015��4��4�� ����11:32:19
     */
	public void initListener(){
		//Note�б����¼�
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent();
				intent.setClass(mContext, NoteInfoDetails.class);
				int index = position-2;
				if(mNoteItemLists.size() >= position && index >=0){
					NoteInfo noteInfo = mNoteItemLists.get(index);
					intent.putExtra("noteInfo", noteInfo);
					startActivity(intent);	
				}else{
					Toast.makeText(mContext, "�޷���ȡ����", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
	}
	/**
	 * 
	 * @author liwei
	 * @Description: ���ڱʼ�Note�첽������
	 * @date:2015��2��16�� ����10:19:52
	 */
	private class NoteLoadAnsyTask extends AsyncTask<String, Void, Void> {

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
				list = NoteInfoClient.getHomeNoteDatafromServer(Integer.parseInt(params[0]),(String)currentUser.get("uid"));
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
//			// TODO Auto-generated method stub
//			if( list !=null){
//				mNoteItemLists.addAll(list);
//			}
		    //�����ˢ�£���ȡ���ݺ��Ӧ��ֹͣˢ��
			if(mListView.getRefresh())
			{
				mListView.stopRefresh();
				mAdapter.clear();
				mAdapter.addAll(list,true);
				mNoteItemLists.addAll(list);
//				mAdapter.notifyDataSetChanged();
			}
			if(mListView.getLoadMore())
			{
				mListView.stopLoadMore();
				mAdapter.addAll(list,true);
				//���ӷ��������ص���
				mNoteItemLists.addAll(list);
			}
			//����Ѿ�ȫ�����ؾ�����Ϊ���ɼ��ظ���,�˴��߼����ڻ�ģ�Ӧ�ø���note�����ж��Ƿ���Լ��ظ���
			if(list.size()==0 ){
				Toast.makeText(mContext, "û�и�������!", Toast.LENGTH_SHORT).show();
				mListView.setPullLoadEnable(false);
			}else{
				
				mListView.setPullLoadEnable(true);
			}
			
		}

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);
	}

	/**
	 * 
	 * @author liwei
	 * @Description: �����б�������
	 * @date:2015��2��15�� ����10:38:40
	 */
	private class HomeDataAdapter extends SetAdapter<NoteInfo> {
		private ViewHolder viewHolder;
		private LayoutInflater inflater;

		public HomeDataAdapter(Context context) {
			super(context);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			if (convertView == null && null != inflater) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.layout_noteitem, null);
				viewHolder.noteImage = (ImageView) convertView
						.findViewById(R.id.iv_picture);
				viewHolder.noteContent = (EmojiconTextView) convertView
						.findViewById(R.id.tv_detail);
				viewHolder.createTime = (TextView) convertView
						.findViewById(R.id.tv_timeline);
				viewHolder.userName = (TextView) convertView
						.findViewById(R.id.tv_user);
				viewHolder.userImage = (ImageView) convertView.findViewById(R.id.user_image);
				viewHolder.commentCount = (TextView) convertView
						.findViewById(R.id.textview_comment_count);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			NoteInfo note = getItem(position);
			String url = null;

		
			if(null != note && null !=viewHolder){
				viewHolder.commentCount.setText(String.valueOf(note.noteComment));
				viewHolder.createTime.setText(note.noteCreateTime);
				viewHolder.noteContent.setText(note.noteContent);
//				Drawable drawable = getResources().getDrawable(R.drawable.someone);
//				Bitmap bit = ImageTool.drawableToBitmap(drawable);
//				Bitmap bitmap = ThumbnailUtils.extractThumbnail(bit, 300, 260);
//				viewHolder.noteImage.setImageBitmap(bitmap);
				
				//�û�ͷ��
				if(!TextUtils.isEmpty(note.userImageUrl.trim())){
					ImageLoader.getInstance().displayImage(note.userImageUrl, viewHolder.userImage,mDisplayUserImage);
				}
				//�ʼ�ͼƬ��������bug����note.noteImageUrl�����������
				if (!TextUtils.isEmpty(note.noteImageUrl) && !TextUtils.isEmpty(note.noteImageUrl.trim())) {
					url = note.noteImageUrl;
					ImageLoader.getInstance().displayImage(url,
							viewHolder.noteImage, mDisplayImageOptions);
				}
				viewHolder.userName.setText(note.userName);
			}
			
			return convertView;
		}
	}

	private class ViewHolder {
		/** �û��� */
		private TextView userName;
		/** �û�ͷ��*/
		private ImageView userImage;
		/** ����ʱ�� */
		private TextView createTime;
		/** ����ı��� */
		private TextView noteTitle;
		/** ��������� */
		private EmojiconTextView noteContent;
		/** �ʼ�ͼƬ */
		private ImageView noteImage;
		/** ���۵���Ŀ */
		private TextView commentCount;
		/** ���޵���Ŀ */
		private TextView colculateCount;
	}

	/**
	 * 
	 * @author liwei
	 * @Description:�б�ˢ�¼���
	 * @date:2015��2��15�� ����10:27:37
	 */
	private class HomeDataListViewListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			Log.d("MainFragment", "����ˢ��");
			if (null == mNoteLoadAnsyTask|| mNoteLoadAnsyTask.getStatus() != Status.RUNNING
					) {
				mNoteLoadAnsyTask = new NoteLoadAnsyTask();
			}
			//����û����ظ���ܶ�Σ���ômPageֵ�ͻ�䣬���Ҫ��֤�û��ڼ��ظ���ܶ����ˢ��һ�ξͱ����Ƚ�mPage����Ϊ0
			mPage = 0;
			//ÿһ���첽��ͬʱֻ��ִ��һ�Σ������Ѿ���ִ��ʱ�Ͳ�����ִ�У����Ա����ȼ��ж�
			if(mNoteLoadAnsyTask.getStatus() != Status.RUNNING){
				mNoteItemLists.clear();
				mNoteLoadAnsyTask.execute(mPage*mPageNumber+"");
			}
		}

		@Override
		public void onLoadMore() {
			//���ظ��࣬��һ�μ��ظ����ʱ�����ǰ5�����Ժ�ÿ�μ��ظ�����Ǻ���ǰmPage*5��
			//���ݵ�ǰ�ж�����Ŀ�������ǵڼ�ҳ
			mPage =  mPage + 1;
			mNoteLoadAnsyTask = new NoteLoadAnsyTask();
			mNoteLoadAnsyTask.execute(mPage*mPageNumber+"");
		}

	}

}
