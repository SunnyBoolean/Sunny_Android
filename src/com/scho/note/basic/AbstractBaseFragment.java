/**
 * 
 */
package com.scho.note.basic;

import android.app.Activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * @author hello
 * @Description:����Fragment�Ļ���
 * @date:2015��1��30��
 */

public class AbstractBaseFragment extends SherlockFragment{
    private ActionBar mActionBar;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActionBar = getSherlockActivity().getSupportActionBar();
	}
	
}
