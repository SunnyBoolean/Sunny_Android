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
 * @date:  2015��4��23��
 */
public class AddNewScheduleActivity extends Activity{
    /** n����*/
	private EditText mScheduEt;
	/** ���*/
	private Button mScheduBt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_schedule);
		mScheduBt = (Button) findViewById(R.id.schedu_complement);
		mScheduEt = (EditText) findViewById(R.id.schedu_content);
	}

}
