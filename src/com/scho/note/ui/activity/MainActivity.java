package com.scho.note.ui.activity;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.note.ui.activity.R;
import com.scho.http.BasicNameValuePair;
import com.scho.note.Constants;
import com.scho.note.basic.activity.BaseMainUIActivity;
import com.scho.note.client.UserInfoClient;
import com.scho.note.entity.UserInfo;
import com.scho.note.sina.AccessTokenKeeper;
import com.scho.note.ui.fragment.DiscoverFragment;
import com.scho.note.ui.fragment.HomeFragment;
import com.scho.note.ui.fragment.RelationsFragment;
import com.scho.note.ui.fragment.ReportFragment;
import com.scho.note.widget.NoteSlidMenu;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 
 * @author ��ΰ
 * @Description:������
 * @date:2015��2��15�� ����12:34:38
 */
public class MainActivity extends BaseMainUIActivity {
	/** �����ؼ������˳����򣬵���ֵΪtrueʱ */
	private boolean mIsExit = false;
	/** �Ź�������Դ */
	private ArrayList<Object> mItems = new ArrayList<Object>();
	/** �����˵� */
	private NoteSlidMenu mNoteSlidMenu;
	/** ��ҳ */
	private RadioButton mHomeRadioButton;
	/** �� */
	private RadioButton mReportRadioButton;
	/** ���� */
	private RadioButton mRelationsButton;
	/** ���� */
	private RadioButton mDiscoverRadioButton;
	/** ��ǰչʾ��RadioButton */
	private RadioButton mFirstPageLayout;
	/** ���ˆ�*/
	private ImageView mMainView;
	/** ����˵���ť*/
	private ImageView mMenuMomre;
	/** ���ð�ť*/
	private TextView mSettingView;
	/** �˵�����*/
	private PopupWindow mMoreWindow;
	/** �û���Ϣ*/
	private ContentValues mUserInfoCV;
	/** �û�ͷ��*/
	private ImageView mUserImage;
	 /** �û�ʵ��*/
	private UserInfo mUserInfo;
	/** ͼƬ*/
	private DisplayImageOptions mDisplayImageOptions;
	 /** ��ǰ Token ��Ϣ */
    private Oauth2AccessToken mAccessToken;
    /** �û���Ϣ�ӿ� */
    private UsersAPI mUsersAPI;
    /** ��ȡ�����˺���Ϣ�Ļص��ӿ�*/
    private SinaRequestListener mSinaListener;
	/** �û�id*/
    private String mUid;
    /** ��ǰ�û�*/
	private AVUser mCurAvUser;
	/** �û��ǳ�*/
	private TextView mUserNmae;
    @Override
    public void initActionBar(){
    	super.initActionBar();
    	mActionBar.setCustomView(R.layout.layout_actionbar_head);
		View actionBar = LayoutInflater.from(mContext).inflate(R.layout.layout_actionbar_head,null);
		mActionBar.setCustomView(actionBar,new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT,ActionBar.LayoutParams.FILL_PARENT));

    }
	@Override
	public void initCompontent() {
		setContentView(R.layout.activity_main);
		mNoteSlidMenu = (NoteSlidMenu) findViewById(R.id.id_menu);
				//��ʼ��Fragment�����¼�
		initFragementView();
		//�û���½���ȡ��Ϣ
		mUserInfoCV = (ContentValues) getIntent().getExtras().get("sina_user");
		mMainView = (ImageView) findViewById(R.id.main_menu);
		mMenuMomre = (ImageView)findViewById(R.id.main_menu_more);
		mSettingView = (TextView) findViewById(R.id.slid_setting);
		mUserImage = (ImageView) findViewById(R.id.user_img);
		mUserNmae = (TextView) findViewById(R.id.user_screen_name);
		mHomeRadioButton.setSelected(true);
		//Ĭ��ѡ����ҳ
//        mHomeRadioButton.setCompoundDrawables(null, getResources().getDrawable(R.drawable.ic_sy_hover), null, null);
        initPopupWindow();
		mDisplayImageOptions = new DisplayImageOptions.Builder().cacheOnDisc()
				.cacheInMemory().showStubImage(R.drawable.placeholder_image)
				.showImageOnFail(R.drawable.placeholder_image)                        //����ͼƬ����ʧ�ܵ�չʾͼƬ
				.displayer(new RoundedBitmapDisplayer(35))   //����Բ��ͼƬ
				.showImageForEmptyUri(R.drawable.placeholder_image).build();
		//��ȡ��ǰ�û���Ϣ
		getCurUser();
		// ��ȡ��ǰ�ѱ������ Token
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        mUid = mUserInfoCV.getAsString(Constants.UserInfo.SINA_USER_ID);
        // ��ȡ�û���Ϣ�ӿ�
        mUsersAPI = new UsersAPI(this, Constants.Sina_Code.APP_KEY, mAccessToken);
        mSinaListener = new SinaRequestListener();
        //�ӵ��أ���UserAPI���У�������show�������������ƣ��ڶ����������ǻص��ӿڣ���һ���������������uid����ѯ��ô�ʹ���long���Ͳ���
        //�������û��ǳƲ��Ǵ����ַ����������������uid����ѯ֮���Ա��������Ϊû�а�Stringת��ΪLong�������˸����û��ǳ�����ѯ�Ľӿڣ���Ȼ�û��������ˣ�
        mUsersAPI.show(Long.parseLong(mUid), mSinaListener);
	}
	/**
	 * ��ȡ��ǰ�û�
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��4��3�� ����11:28:53
	 */
	public void getCurUser(){
		AVUser currentUser = AVUser.getCurrentUser();
		if (currentUser != null) {
		    // �����û�ʹ��Ӧ��
			mCurAvUser = currentUser;
		} else {
		    //�����û�����Ϊ��ʱ�� �ɴ��û�ע����桭
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
                     Toast.makeText(mContext,"��ȡUser��Ϣ�ɹ����û��ǳƣ�" + user.screen_name+"    �û�ͷ���ַ:"+user.avatar_large, 
                             Toast.LENGTH_LONG).show();
                     ImageLoader.getInstance().displayImage(user.avatar_large,
                     		mUserImage, mDisplayImageOptions);
                     //�����û��ǳ�
                     mUserNmae.setText(user.screen_name);
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
   
	@Override
	public void initSourceData() {
	}

	@Override
	public void initAdapter() {
	}

	@Override
	public void initListener() {
        SingleClickListener clickListener = new SingleClickListener();
        mMainView.setOnClickListener(clickListener);
        mMenuMomre.setOnClickListener(clickListener);
        mSettingView.setOnClickListener(clickListener);
	}

	@Override
	public void handleDetailMsg(Message msg) {
		mIsExit = false;
		super.handleDetailMsg(msg);
	}

	/**
	 * 
	 * Description: RadioButton�ĳ�rʼ���Լ������¼�ע��
	 * 
	 * @author: liwei
	 * @date:2015��2��15�� ����12:43:24
	 */
	public void initFragementView() {
		mHomeRadioButton = (RadioButton) findViewById(R.id.home_btn);
		mHomeRadioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changedRadioButtonByClick(mHomeRadioButton);
			}
		});
		mReportRadioButton = (RadioButton) findViewById(R.id.repord_btn);
		mReportRadioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//���ж��û��Ƿ��½
				if(checkIsLogin()){
					changedRadioButtonByClick(mReportRadioButton);
				}else{
					//��ת����½����
				}
			}
		});
		mRelationsButton = (RadioButton) findViewById(R.id.relations_btn);
		mRelationsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//���ж��û��Ƿ��½
				if(checkIsLogin()){
				changedRadioButtonByClick(mRelationsButton);
				}else{
					//��ת����½����
				}
			}
		});
		mDiscoverRadioButton = (RadioButton) findViewById(R.id.discover_btn);
		mDiscoverRadioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changedRadioButtonByClick(mDiscoverRadioButton);
			}
		});
		//���û�����Ӧ��ʱ����չʾ������ҳ����
			changedRadioButtonByClick(mHomeRadioButton);
	}

	/**
	 * 
	 * Description: ����RadioButtonʱ��ͬFragment֮����ת������ʵ�ֵײ��ĸ�Fragment�ĳ�ʼ���Լ���ȡ
	 * 
	 * @author: ��ΰ
	 * @date:2015��2��15�� ����12:52:35
	 */
	public void changedRadioButtonByClick(RadioButton buttonView) {
		if (buttonView == mFirstPageLayout)
			return;
		// ȡ��FM����
		FragmentManager fm = getSupportFragmentManager();
		// �������֮��һ��Ҫ�ǵ��ύ������add��Ч
		FragmentTransaction ft = fm.beginTransaction();
		if (fm.getFragments() != null) {
			for (Fragment fragment : fm.getFragments()) {
				// ���û�����ؾ�����֮
				if (!fragment.isHidden()) {
					ft.hide(fragment);
				}
			}
		}
		switch (buttonView.getId()) {
		//��ҳ
		case R.id.home_btn:
			HomeFragment homeFragment = (HomeFragment) fm
					.findFragmentByTag("home_f");
			if (homeFragment == null) {
				homeFragment = new HomeFragment();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, homeFragment, "home_f");
			}
			// ��ʾ��ҳ
			ft.show(homeFragment);
			break;
			//��
		case R.id.repord_btn:
			ReportFragment reportFragment = (ReportFragment) fm
					.findFragmentByTag("report_f");
			if (reportFragment == null) {
				reportFragment = new ReportFragment();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, reportFragment, "report_f");
			}
			// ��ʾ�򿨽���
			ft.show(reportFragment);
			break;
			//����
		case R.id.relations_btn:
			RelationsFragment relationFragment = (RelationsFragment) fm
					.findFragmentByTag("relation_f");
			if (relationFragment == null) {
				relationFragment = new RelationsFragment();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, relationFragment, "relation_f");
			}
			// ��ʾ������ҳ
			ft.show(relationFragment);
			break;
			//����
		case R.id.discover_btn:
			DiscoverFragment discoverFragment = (DiscoverFragment) fm
					.findFragmentByTag("discover_f");
			if (discoverFragment == null) {
				discoverFragment = new DiscoverFragment();
				// ����Fragment��ӵ�������,��һ��������Fragment�ĸ��������ڶ�����������Ҫ����Fragment��������������tag����ʹ��findFragmentByTag��������
				ft.add(R.id.maintabcontent, discoverFragment, "discover_f");
			}
			// ��ʾ���ֽ���
			ft.show(discoverFragment);
			break;
		default:
			break;
			
		}
		ft.commitAllowingStateLoss();
	}

	/**
	 * �Ź���������
	 * 
	 * @author hello
	 * 
	 */
	
	private class GridViewAdapter extends BaseAdapter {
		private final ArrayList<GridItem> mItems;
		LayoutInflater infalter;

		public GridViewAdapter(ArrayList<GridItem> items) {
			mItems = items;
			infalter = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mItems) {
				return mItems.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int arg0) {
			if (null != mItems) {
				return mItems.get(arg0);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parentView) {
			GridItem item = this.mItems.get(position);
			ViewHolder viewHolder;
			if (null != convertView) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {

				viewHolder = new ViewHolder();
				// ��ȡ��ͼ
				convertView = infalter.inflate(R.layout.layout_nine_grid, null);
				viewHolder.grid_img = (ImageView) convertView
						.findViewById(R.id.nine_grid_img);
				viewHolder.grid_text = (TextView) convertView
						.findViewById(R.id.nine_grid_text);
				convertView.setTag(viewHolder);
			}
			// ������ͼ����Դ
			viewHolder.grid_img.setImageResource(item.icon);
			viewHolder.grid_text.setText(item.name);
			return convertView;
		}

		private class ViewHolder {
			ImageView grid_img;
			TextView grid_text;
		}
	}

	/**
	 * �Ź��񵥻��¼�
	 * 
	 * @author hello
	 * 
	 */
	private class GridItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if ((position < 0) || (position >= MainActivity.this.mItems.size())) {
				return;
			}
			Intent intent = new Intent();
			startActivity(intent);
		}
	}

	/**
	 * GridView��װ�������������ԣ�ͼ�ꡢ��������������ת��Ŀ��Activity
	 * 
	 * @author hello
	 * 
	 */
	private class GridItem {
		// ����
		public String name = "";
		// ͼ��
		public int icon = 0;
		// ������ת����Activity
		public Class<?> cls = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * �ж��Ƿ�����˳�����
	 */
	public void exit() {
		// ���Ϊfalse�ͽ�����Ϊtrue��������������û�û�а��ڶ��ξ�������Ϊfalse
		// �����û�������2�������������η��ؼ��ſ����˳�����
		if (!mIsExit) {
			mIsExit = true;
			Toast.makeText(mContext, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_menu:
			mNoteSlidMenu.toggle();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	 * ����Ƿ��½
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��3��7�� ����2:39:07
	 */
	public boolean checkIsLogin(){
		
		return true;
	}
	public void initPopupWindow(){
		//����
		TextView pianoTv;
		//¼��
		TextView recordTv;
		//�Խ���
		TextView interPhoneTv;
		View v = LayoutInflater.from(mContext).inflate(R.layout.layout_more, null);
		mMoreWindow = new PopupWindow(v,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        mMoreWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_selector));
        mMoreWindow.setOutsideTouchable(true);
	}
	/**
	 * 
	 * @author liwei
	 * @Description:�����¼�
	 * @date:2015��2��6�� ����3:24:27
	 */
	private class SingleClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent startIntent;
			switch (v.getId()) {
			case R.id.main_menu:
				mNoteSlidMenu.toggle();
				break;
			case R.id.slid_setting:
				startIntent = new Intent();
				startIntent.setClass(mContext, SettingActivity.class);
				startActivity(startIntent);
			case R.id.main_menu_more:
				if(mMoreWindow !=null && mMoreWindow.isShowing()){
					mMoreWindow.dismiss();
				}else{
					mMoreWindow.showAsDropDown(mMenuMomre,0,38);
				}
					
				break;
			default:
				break;
			}
		}
	}
	
	private class LoadUserInfoTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... arg0) {
        	ArrayList<BasicNameValuePair> basicList = new ArrayList<BasicNameValuePair>();
        	basicList.add(new BasicNameValuePair(Constants.UserInfo.SINA_APP_KEY,Constants.Sina_Code.APP_KEY));
        	basicList.add(new BasicNameValuePair(Constants.UserInfo.SINA_ACCESS_TOKEN, mUserInfoCV.getAsString(Constants.UserInfo.SINA_ACCESS_TOKEN)));
        	basicList.add(new BasicNameValuePair(Constants.UserInfo.SINA_USER_ID, mUserInfoCV.getAsString(Constants.UserInfo.SINA_USER_ID)));
        	mUserInfo = new UserInfoClient().getUserBaseInfo(Constants.Sina_Code.SINA_USER_INFO_URL, basicList, "GET");
			
        	return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            ImageLoader.getInstance().displayImage(mUserInfo.sinaUserImageUrl,
            		mUserImage, mDisplayImageOptions);
        }
    }
	   

}
