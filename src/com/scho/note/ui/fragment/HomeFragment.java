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
 * @Description:主界面展示的Home首页Fragment
 * @date:2015年2月13日 下午9:14:18
 */

public class HomeFragment extends BaseHomeFragment {
	/** 展示列表 */
	private XListView mListView;
	/** 适配器 */
	private HomeDataAdapter mAdapter;
	/** 加载笔记内容中的图片*/
	private DisplayImageOptions mDisplayImageOptions;
	/** 加载笔记中用户的头像*/
	private DisplayImageOptions mDisplayUserImage;
	/** 刷新监听器 */
	private HomeDataListViewListener mHomeDataListViewListener;
	/** note公众笔记加载类 */
	private NoteLoadAnsyTask mNoteLoadAnsyTask;
	/** 从服务器加载的数据源 */
	private List<NoteInfo> mNoteItemLists = new ArrayList<NoteInfo>();
	/** 每次加载更多或刷新的时候获取到的数据，每次获取数据都会添加到mNoteItemLists中*/
	private ArrayList<NoteInfo> list;
	/** 上下文對象*/
	private Context mContext;
	/** 首页显示的页数，默认是一页，每次加载更多就加载一页*/
	private int mPage = 0;
	/** 每加载一页显示的页数*/
	private final int mPageNumber = 5;
	/** 当前列表状态，是否正在刷新*/
	private boolean mIsRefreshing = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化笔记内容图片参数
		mDisplayImageOptions = new DisplayImageOptions.Builder().cacheOnDisc()
				.cacheInMemory().showStubImage(R.drawable.placeholder_image)
				.showImageOnFail(R.drawable.placeholder_image)
				.showImageForEmptyUri(R.drawable.placeholder_image).build();
		//初始化获取发表用户的头像参数
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
		//ListView下拉刷新等监听
		mListView.setXListViewListener(mHomeDataListViewListener);
		View va = inflater.inflate(R.layout.layout_home_time_comp, null);
		mListView.addHeaderView(va,null,false);
		mAdapter = new HomeDataAdapter(mContext);
		mListView.setAdapter(mAdapter);
		//默认为不能下拉刷新
		mListView.setPullLoadEnable(false);
		//列表单击事件
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
		//第一次进来时首先进行刷新
		mListView.startRefresh(true);
		//单击事件监听初始化
		initListener();
		return v;
	}
	/**
     * 监听器初始化
     * Description: TODO
     * @author: 李伟
     * @date:2015年4月4日 下午11:32:19
     */
	public void initListener(){
		//Note列表单击事件
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
					Toast.makeText(mContext, "无法获取详情", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
	}
	/**
	 * 
	 * @author liwei
	 * @Description: 公众笔记Note异步加载类
	 * @date:2015年2月16日 下午10:19:52
	 */
	private class NoteLoadAnsyTask extends AsyncTask<String, Void, Void> {

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
		    //如果是刷新，获取数据后就应该停止刷新
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
				//将从服务器加载的数
				mNoteItemLists.addAll(list);
			}
			//如果已经全部加载就设置为不可加载更多,此处逻辑后期会改，应该根据note总数判断是否可以加载更多
			if(list.size()==0 ){
				Toast.makeText(mContext, "没有更多数据!", Toast.LENGTH_SHORT).show();
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
	 * @Description: 数据列表适配器
	 * @date:2015年2月15日 下午10:38:40
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
				
				//用户头像
				if(!TextUtils.isEmpty(note.userImageUrl.trim())){
					ImageLoader.getInstance().displayImage(note.userImageUrl, viewHolder.userImage,mDisplayUserImage);
				}
				//笔记图片，产生了bug，是note.noteImageUrl控制真引起的
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
		/** 用户名 */
		private TextView userName;
		/** 用户头像*/
		private ImageView userImage;
		/** 发表时间 */
		private TextView createTime;
		/** 发表的标题 */
		private TextView noteTitle;
		/** 发表的内容 */
		private EmojiconTextView noteContent;
		/** 笔记图片 */
		private ImageView noteImage;
		/** 评论的数目 */
		private TextView commentCount;
		/** 被赞的数目 */
		private TextView colculateCount;
	}

	/**
	 * 
	 * @author liwei
	 * @Description:列表刷新监听
	 * @date:2015年2月15日 下午10:27:37
	 */
	private class HomeDataListViewListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			Log.d("MainFragment", "正在刷新");
			if (null == mNoteLoadAnsyTask|| mNoteLoadAnsyTask.getStatus() != Status.RUNNING
					) {
				mNoteLoadAnsyTask = new NoteLoadAnsyTask();
			}
			//如果用户加载更多很多次，那么mPage值就会变，因此要保证用户在加载跟多很多次再刷新一次就必须先将mPage设置为0
			mPage = 0;
			//每一个异步类同时只能执行一次，当他已经在执行时就不能在执行，所以必须先加判断
			if(mNoteLoadAnsyTask.getStatus() != Status.RUNNING){
				mNoteItemLists.clear();
				mNoteLoadAnsyTask.execute(mPage*mPageNumber+"");
			}
		}

		@Override
		public void onLoadMore() {
			//加载更多，第一次加载更多的时候忽略前5条，以后每次加载更多就是忽略前mPage*5条
			//根据当前有多少条目来计算是第几页
			mPage =  mPage + 1;
			mNoteLoadAnsyTask = new NoteLoadAnsyTask();
			mNoteLoadAnsyTask.execute(mPage*mPageNumber+"");
		}

	}

}
