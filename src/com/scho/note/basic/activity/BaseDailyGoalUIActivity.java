/**
 * 
 */
package com.scho.note.basic.activity;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.scho.note.basic.AbstractBaseActivity;

/**
 * @author liwei
 * @Description:ÿ��Ŀ��ģ��Ļ��࣬�ù���ģ���µ�����Activity���̳д���
 * @date:2015��1��31��
 */

public class BaseDailyGoalUIActivity extends AbstractBaseActivity {

	@Override
	public void initActionBar() {
		super.initActionBar();
		mActionBar.setHomeButtonEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.note.basic.AbstractBaseActivity#initCompontent()
	 */
	@Override
	public void initCompontent() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.note.basic.AbstractBaseActivity#initSourceData()
	 */
	@Override
	public void initSourceData() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.note.basic.AbstractBaseActivity#initAdapter()
	 */
	@Override
	public void initAdapter() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.note.basic.AbstractBaseActivity#initListener()
	 */
	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
}
