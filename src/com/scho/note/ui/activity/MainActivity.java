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
 * @author 李伟
 * @Description:主界面
 * @date:2015年2月15日 上午12:34:38
 */
public class MainActivity extends BaseMainUIActivity {
	/** 按返回键两次退出程序，当此值为true时 */
	private boolean mIsExit = false;
	/** 九宫格数据源 */
	private ArrayList<Object> mItems = new ArrayList<Object>();
	/** 滑动菜单 */
	private NoteSlidMenu mNoteSlidMenu;
	/** 首页 */
	private RadioButton mHomeRadioButton;
	/** 打卡 */
	private RadioButton mReportRadioButton;
	/** 人脉 */
	private RadioButton mRelationsButton;
	/** 发现 */
	private RadioButton mDiscoverRadioButton;
	/** 当前展示的RadioButton */
	private RadioButton mFirstPageLayout;
	/** 主菜單*/
	private ImageView mMainView;
	/** 更多菜单按钮*/
	private ImageView mMenuMomre;
	/** 设置按钮*/
	private TextView mSettingView;
	/** 菜单更多*/
	private PopupWindow mMoreWindow;
	/** 用户信息*/
	private ContentValues mUserInfoCV;
	/** 用户头像*/
	private ImageView mUserImage;
	 /** 用户实体*/
	private UserInfo mUserInfo;
	/** 图片*/
	private DisplayImageOptions mDisplayImageOptions;
	 /** 当前 Token 信息 */
    private Oauth2AccessToken mAccessToken;
    /** 用户信息接口 */
    private UsersAPI mUsersAPI;
    /** 获取新浪账号信息的回调接口*/
    private SinaRequestListener mSinaListener;
	/** 用户id*/
    private String mUid;
    /** 当前用户*/
	private AVUser mCurAvUser;
	/** 用户昵称*/
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
				//初始化Fragment单击事件
		initFragementView();
		//用户登陆后获取信息
		mUserInfoCV = (ContentValues) getIntent().getExtras().get("sina_user");
		mMainView = (ImageView) findViewById(R.id.main_menu);
		mMenuMomre = (ImageView)findViewById(R.id.main_menu_more);
		mSettingView = (TextView) findViewById(R.id.slid_setting);
		mUserImage = (ImageView) findViewById(R.id.user_img);
		mUserNmae = (TextView) findViewById(R.id.user_screen_name);
		mHomeRadioButton.setSelected(true);
		//默认选中首页
//        mHomeRadioButton.setCompoundDrawables(null, getResources().getDrawable(R.drawable.ic_sy_hover), null, null);
        initPopupWindow();
		mDisplayImageOptions = new DisplayImageOptions.Builder().cacheOnDisc()
				.cacheInMemory().showStubImage(R.drawable.placeholder_image)
				.showImageOnFail(R.drawable.placeholder_image)                        //设置图片加载失败的展示图片
				.displayer(new RoundedBitmapDisplayer(35))   //设置圆角图片
				.showImageForEmptyUri(R.drawable.placeholder_image).build();
		//读取当前用户信息
		getCurUser();
		// 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        mUid = mUserInfoCV.getAsString(Constants.UserInfo.SINA_USER_ID);
        // 获取用户信息接口
        mUsersAPI = new UsersAPI(this, Constants.Sina_Code.APP_KEY, mAccessToken);
        mSinaListener = new SinaRequestListener();
        //坑爹呢，在UserAPI类中，有两个show方法，参数类似，第二个参数都是回调接口，第一个参数，如果根据uid来查询那么就传入long类型参数
        //而根据用户昵称才是传入字符串！所以这里，根据uid来查询之所以报错就是因为没有把String转换为Long而调用了根据用户昵称来查询的接口！当然用户不存在了！
        mUsersAPI.show(Long.parseLong(mUid), mSinaListener);
	}
	/**
	 * 获取当前用户
	 * Description: TODO
	 * @author: 李伟
	 * @date:2015年4月3日 下午11:28:53
	 */
	public void getCurUser(){
		AVUser currentUser = AVUser.getCurrentUser();
		if (currentUser != null) {
		    // 允许用户使用应用
			mCurAvUser = currentUser;
		} else {
		    //缓存用户对象为空时， 可打开用户注册界面…
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
                     Toast.makeText(mContext,"获取User信息成功，用户昵称：" + user.screen_name+"    用户头像地址:"+user.avatar_large, 
                             Toast.LENGTH_LONG).show();
                     ImageLoader.getInstance().displayImage(user.avatar_large,
                     		mUserImage, mDisplayImageOptions);
                     //设置用户昵称
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
	 * Description: RadioButton的初r始化以及单击事件注册
	 * 
	 * @author: liwei
	 * @date:2015年2月15日 上午12:43:24
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
				//先判断用户是否登陆
				if(checkIsLogin()){
					changedRadioButtonByClick(mReportRadioButton);
				}else{
					//跳转到登陆界面
				}
			}
		});
		mRelationsButton = (RadioButton) findViewById(R.id.relations_btn);
		mRelationsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//先判断用户是否登陆
				if(checkIsLogin()){
				changedRadioButtonByClick(mRelationsButton);
				}else{
					//跳转到登陆界面
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
		//当用户进入应用时首先展示的是首页界面
			changedRadioButtonByClick(mHomeRadioButton);
	}

	/**
	 * 
	 * Description: 单机RadioButton时不同Fragment之间跳转，这里实现底部四个Fragment的初始化以及获取
	 * 
	 * @author: 李伟
	 * @date:2015年2月15日 上午12:52:35
	 */
	public void changedRadioButtonByClick(RadioButton buttonView) {
		if (buttonView == mFirstPageLayout)
			return;
		// 取得FM对象
		FragmentManager fm = getSupportFragmentManager();
		// 开启事物，之后一定要记得提交，否则add无效
		FragmentTransaction ft = fm.beginTransaction();
		if (fm.getFragments() != null) {
			for (Fragment fragment : fm.getFragments()) {
				// 如果没有隐藏就隐藏之
				if (!fragment.isHidden()) {
					ft.hide(fragment);
				}
			}
		}
		switch (buttonView.getId()) {
		//首页
		case R.id.home_btn:
			HomeFragment homeFragment = (HomeFragment) fm
					.findFragmentByTag("home_f");
			if (homeFragment == null) {
				homeFragment = new HomeFragment();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, homeFragment, "home_f");
			}
			// 显示首页
			ft.show(homeFragment);
			break;
			//打卡
		case R.id.repord_btn:
			ReportFragment reportFragment = (ReportFragment) fm
					.findFragmentByTag("report_f");
			if (reportFragment == null) {
				reportFragment = new ReportFragment();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, reportFragment, "report_f");
			}
			// 显示打卡界面
			ft.show(reportFragment);
			break;
			//人脉
		case R.id.relations_btn:
			RelationsFragment relationFragment = (RelationsFragment) fm
					.findFragmentByTag("relation_f");
			if (relationFragment == null) {
				relationFragment = new RelationsFragment();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, relationFragment, "relation_f");
			}
			// 显示人脉首页
			ft.show(relationFragment);
			break;
			//发现
		case R.id.discover_btn:
			DiscoverFragment discoverFragment = (DiscoverFragment) fm
					.findFragmentByTag("discover_f");
			if (discoverFragment == null) {
				discoverFragment = new DiscoverFragment();
				// 将该Fragment添加到事物中,第一个参数是Fragment的父容器，第二个参数是需要填充的Fragment，第三个参数是tag可以使用findFragmentByTag（）查找
				ft.add(R.id.maintabcontent, discoverFragment, "discover_f");
			}
			// 显示发现界面
			ft.show(discoverFragment);
			break;
		default:
			break;
			
		}
		ft.commitAllowingStateLoss();
	}

	/**
	 * 九宫格适配器
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
				// 获取视图
				convertView = infalter.inflate(R.layout.layout_nine_grid, null);
				viewHolder.grid_img = (ImageView) convertView
						.findViewById(R.id.nine_grid_img);
				viewHolder.grid_text = (TextView) convertView
						.findViewById(R.id.nine_grid_text);
				convertView.setTag(viewHolder);
			}
			// 设置视图的资源
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
	 * 九宫格单击事件
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
	 * GridView的装载器，三个属性，图标、文字描述、以跳转的目的Activity
	 * 
	 * @author hello
	 * 
	 */
	private class GridItem {
		// 名称
		public String name = "";
		// 图像
		public int icon = 0;
		// 单击跳转到的Activity
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
	 * 判断是否可以退出程序
	 */
	public void exit() {
		// 如果为false就将其置为true，如果过了两秒用户没有按第二次就又设置为false
		// 即，用户必须在2秒内连续按两次返回键才可以退出程序
		if (!mIsExit) {
			mIsExit = true;
			Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
	 * 检查是否登陆
	 * Description: TODO
	 * @author: 李伟
	 * @date:2015年3月7日 下午2:39:07
	 */
	public boolean checkIsLogin(){
		
		return true;
	}
	public void initPopupWindow(){
		//钢琴
		TextView pianoTv;
		//录音
		TextView recordTv;
		//对讲机
		TextView interPhoneTv;
		View v = LayoutInflater.from(mContext).inflate(R.layout.layout_more, null);
		mMoreWindow = new PopupWindow(v,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        mMoreWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_selector));
        mMoreWindow.setOutsideTouchable(true);
	}
	/**
	 * 
	 * @author liwei
	 * @Description:单击事件
	 * @date:2015年2月6日 下午3:24:27
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
