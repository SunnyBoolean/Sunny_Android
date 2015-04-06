/**
 * 
 */
package com.scho.note.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.note.ui.activity.R;
import com.scho.note.Constants;
import com.scho.note.basic.activity.BaseSecondActivity;
import com.scho.note.photocamara.Bimp;
import com.scho.note.photocamara.FileUtils;
import com.scho.note.photocamara.PhotoActivity;
import com.scho.note.utils.AnimationUtil;
import com.scho.note.utils.KeyBoardUtils;

/**
 * @author ��ΰ
 * @Description:TODO
 * @date:2015��3��7�� ����9:52:13
 */

public class AddNewNoteActivity extends BaseSecondActivity {
	private ImageView mAddImage;
	/** ѡ�������Դ�˵� */
	private LinearLayout mSelectImageMenu;
	/** ������ȡ��Ƭ */
	private TextView mPictureFromAlbulm;
	/** ���� */
	private TextView mTakePhoto;
	/** ȡ�� */
	private TextView mCancelPhoto;
	/** GridView*/
	private GridView noScrollgridview;
	/** �û���ѡ��ͼƬ������*/
	private GridAdapter adapter;
	/** ����*/
	private TextView mSendNote;
	/** �ϴ����� */
	private AVObject avoNote;
	/** �ļ����� */
	private AVFile avFile;
	/** ��ȡ�ı�ֵ */
	private EditText mEdittem;
	/** �û�id*/
	private String mUid;
	/** �ϴ�����*/
	private ProgressDialog mProgressSending;

	@Override
	public void initCompontent() {
		setContentView(R.layout.layout_add_newnote);
		// mAddImage = (ImageView) findViewById(R.id.add_image);
		mSelectImageMenu = (LinearLayout) findViewById(R.id.takephoto_diago);
		mPictureFromAlbulm = (TextView) findViewById(R.id.getfromphoto);
		mTakePhoto = (TextView) findViewById(R.id.getfrom_takephoto);
		mCancelPhoto = (TextView) findViewById(R.id.getphoto_cancel);
		mSendNote = (TextView) findViewById(R.id.send_note);
		mEdittem = (EditText) findViewById(R.id.note_text);
		//��ȡuid
		mUid = getIntent().getStringExtra("uid");
		Init();
		
	}

	@Override
	public void initListener() {
		// �����Ȼص����෽����ʼ��һ��OnClickListenerʵ��
		super.initListener();
		// mAddImage.setOnClickListener(mSingleListener);
		mPictureFromAlbulm.setOnClickListener(mSingleListener);
		mTakePhoto.setOnClickListener(mSingleListener);
		mCancelPhoto.setOnClickListener(mSingleListener);
		mSendNote.setOnClickListener(mSingleListener);
	}

	@Override
	public void handlerOnClick(View v) {
		switch (v.getId()) {
	
		case R.id.getfromphoto:
			// ��ϵͳ���
			Intent intent = new Intent();
			intent.setClass(mContext, ShowAlbumDirActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
			break;
		case R.id.getfrom_takephoto:
			// ����
			takePhoto();
			break;
		case R.id.getphoto_cancel:
			// ȡ��
			Animation backRnimation = AnimationUtil.translateAnimationInY(0, 2,
					500);
			// ���ò�ֵ��
			AnticipateOvershootInterpolator backant = new AnticipateOvershootInterpolator(
					2.0f);

			backRnimation.setInterpolator(backant);
			// ����ִ������״̬
			backRnimation.setFillAfter(true);
			mSelectImageMenu.startAnimation(backRnimation);
			break;
		case R.id.send_note:
			if(mProgressSending == null){
				mProgressSending = new ProgressDialog(mContext);
				mProgressSending.setMessage("������...");
			}
			mProgressSending.show();
			//�ڷ���������note��
			avoNote = new AVObject(Constants.TableName.TABLE_NOTE);
			String textContent = mEdittem.getText().toString();
			//��ǰ�û�id
			avoNote.put("uid", mUid);
			//Note��id��Ψһ��־
			avoNote.put("noteId", UUID.randomUUID().toString());
			//�ռǵ�����
			avoNote.put("Content", textContent);
			String imageId = UUID.randomUUID().toString();
			for(int i=0;i<Bimp.bmp.size();i++){
				Bitmap bitMap = Bimp.bmp.get(i);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				avFile = new AVFile(imageId, stream.toByteArray());
			}
			//���ͼƬ����null�ͱ���ͼƬ��ͬʱ��ͼƬid�����ڸ��ı���
			if(avFile !=null){
				// ����ͼƬ
				avFile.saveInBackground();
				avoNote.put("noteImage", avFile);
			}
			//�ϴ��ı�
			avoNote.saveInBackground(new SaveCallback() {
			    public void done(AVException e) {
			        if (e == null) {
			        	if( mProgressSending !=null){
			        		mProgressSending.dismiss();
			        	}
			            // ����ɹ�
			        	//���ͼƬ
			        	Bimp.bmp.clear();
			        	
			        	finish();
			        } else {
			            // ����ʧ�ܣ����������Ϣ
			        	Toast.makeText(mContext, "����ʧ�ܣ��������磡", Toast.LENGTH_SHORT).show();
			        }
			    }
			});

			
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE:

			break;
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9) {
				Bimp.drr.add(path);
			}
			break;
		default:
			break;
		}
	}

	public void Init() {
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				 //�رռ���
                 KeyBoardUtils.closeKeybord(mEdittem, mContext);
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(mContext, noScrollgridview);
				} else {
					Intent intent = new Intent(mContext, PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;
		public boolean isShape() {
			return shape;
		}
		public void setShape(boolean shape) {
			this.shape = shape;
		}
		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}
		public void update() {
			loading();
		}
		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}
		public Object getItem(int arg0) {

			return null;
		}
		public long getItemId(int arg0) {

			return 0;
		}
		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}
		public int getSelectedPosition() {
			return selectedPosition;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					takePhoto();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(AddNewNoteActivity.this,
							ShowAlbumDirActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";
	/**
	 * ����
	 * Description: TODO
	 * @author: ��ΰ
	 * @date:2015��4��4�� ����3:55:24
	 */
	public void takePhoto() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/noteImage/", String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}
}
