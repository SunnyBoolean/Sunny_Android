package com.scho.note.photocamara;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;





import com.note.ui.activity.R;
import com.scho.note.basic.activity.BaseSecondActivity;
import com.scho.note.photocamara.ImageGridAdapter.TextCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
/**
 * 
 * @author:  hello
 * @Description:  展示每一个相册下面的所有图片 
 * @date:  2015年2月11日
 */
public class ImageGridActivity extends BaseSecondActivity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
    /** 保存当前目录下的所有图片*/
	List<ImageItem> dataList;
	/** GridView*/
	GridView gridView;
	ImageGridAdapter adapter;//图片适配器
	AlbumHelper helper;
	Button bt;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择9张图片", 400).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void initCompontent() {

		setContentView(R.layout.activity_image_grid);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);

		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//存放选中的图片的url地址，在单击完成后跳转到编辑界面展示
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}

				if (Bimp.act_bool) {
//					Intent intent = new Intent(ImageGridActivity.this,
//							PublishedActivity.class);
//					startActivity(intent);
					Bimp.act_bool = false;
					setResult(RESPONCE_CODE);
					finish();
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.drr.size() < 9) {
						Bimp.drr.add(list.get(i));
					}
				}
				finish();
			}

		});
	}

	/**
	 * 初始化数据
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});

	}
}
