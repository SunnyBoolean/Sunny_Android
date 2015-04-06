/**
 * 
 */
package com.scho.note.basic.activity;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;
import com.scho.note.basic.AbstractBaseActivity;

/**
 * @author hello
 * @Description:TODO 
 * @date:2015��1��30��
 */

public class BaseApisUIActivity extends AbstractBaseActivity{
	protected SlidingActivityBase mSliding;
	@Override
	public void initActionBar() {
		super.initActionBar();
		mActionBar.setHomeButtonEnabled(true);
	}


	@Override
	public void initCompontent() {
		//������ʵ����һ��SlidingActivity����
       if(this instanceof SlidingActivityBase){
    	   mSliding = (SlidingActivityBase) this;
    	   if(mSliding != null && mSliding.getSlidingMenu() !=null){
    		   //���û���ģʽ
    		   mSliding.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    	   }
       }
	}


	@Override
	public void initSourceData() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.note.basic.AbstractBaseActivity#initAdapter()
	 */
	@Override
	public void initAdapter() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
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
