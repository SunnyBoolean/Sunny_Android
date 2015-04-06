package com.scho.note.ui.activity;

import java.io.Serializable;
import java.util.List;

import com.note.ui.activity.R;
import com.scho.note.basic.activity.BaseSecondActivity;
import com.scho.note.photocamara.AlbumHelper;
import com.scho.note.photocamara.ImageBucket;
import com.scho.note.photocamara.ImageBucketAdapter;
import com.scho.note.photocamara.ImageGridActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ShowAlbumDirActivity extends BaseSecondActivity {
	
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	
	@Override
	public void initCompontent() {
		
		setContentView(R.layout.activity_image_bucket);
		helper = AlbumHelper.getHelper();
		helper.init(mContext);

		initData();
		initView();
	}

	/**
	 */
	private void initData() {
		dataList = helper.getImagesBucketList(false);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(ShowAlbumDirActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ShowAlbumDirActivity.this,
						ImageGridActivity.class);
				intent.putExtra(ShowAlbumDirActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				startActivity(intent);
				finish();
			}

		});
	}
}
