/**
 * 
 */
package com.scho.note.basic.activity;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.scho.note.basic.AbstractBaseActivity;

/**
 * @author liwei
 * @Description:每日目标模块的基类，该功能模块下的所有Activity都继承此类
 * @date:2015年1月31日
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
