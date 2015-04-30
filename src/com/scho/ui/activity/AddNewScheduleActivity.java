/**
 * 
 */
package com.scho.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.scho.ui.R;

/**
 * @author:  liwei
 * @Description:  TODO 
 * @date:  2015年4月23日
 */
public class AddNewScheduleActivity extends Activity{
    /** n内容*/
	private EditText mScheduEt;
	/** 完成*/
	private Button mScheduBt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_schedule);
		mScheduBt = (Button) findViewById(R.id.schedu_complement);
		mScheduEt = (EditText) findViewById(R.id.schedu_content);
	}

}
