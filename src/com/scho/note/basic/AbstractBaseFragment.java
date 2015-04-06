/**
 * 
 */
package com.scho.note.basic;

import android.app.Activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * @author hello
 * @Description:所有Fragment的基类
 * @date:2015年1月30日
 */

public class AbstractBaseFragment extends SherlockFragment{
    private ActionBar mActionBar;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActionBar = getSherlockActivity().getSupportActionBar();
	}
	
}
