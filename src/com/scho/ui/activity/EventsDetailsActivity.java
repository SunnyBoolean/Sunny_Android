/**
 * 
 */
package com.scho.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.scho.entity.EventsInfo;
import com.scho.ui.R;

/**
 * @author:  liwei
 * @Description:  У԰����� 
 * @date:  2015��4��17��
 */
public class EventsDetailsActivity extends Activity{
	/** �������*/
	private TextView mEventPerson;
	/** ���ʼʱ��*/
	private TextView mEventStartTime;
	/** �����ʱ��*/
	private TextView mEventEndTime;
	/** �����*/
	private TextView mEventContent;
	/** ��ص��*/
	private TextView mEventLocation;
	/** ActionBar*/
    private ActionBar mActionBar;
    /** �ʵ����*/
    private EventsInfo mCurEventInfo;
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);
		mCurEventInfo = (EventsInfo) getIntent().getSerializableExtra("event");
		mEventPerson = (TextView) findViewById(R.id.events_user);
		mEventContent = (TextView) findViewById(R.id.evt_content);
		mEventEndTime = (TextView) findViewById(R.id.end_time);
		mEventLocation = (TextView) findViewById(R.id.evt_location);
		mEventStartTime = (TextView) findViewById(R.id.start_time);
		initActionBar();
		initCompontent();
	}
	/**
	 * ��ʼ��ȥActionBar
	 */
	public void initActionBar(){
		mActionBar = getActionBar();
		mActionBar.setTitle("�����");
		mActionBar.setIcon(R.drawable.ic_pre);
		mActionBar.setHomeButtonEnabled(true);
	}
	/**
	 * ��ʼ������
	 */
	private void initCompontent(){
		
		if(mCurEventInfo !=null){
		mEventContent.setText(mCurEventInfo.eventContent);
		mEventEndTime.setText(mCurEventInfo.endTime);
		mEventLocation.setText(mCurEventInfo.eventLocation);
		mEventPerson.setText(mCurEventInfo.personName);
		mEventStartTime.setText(mCurEventInfo.startTime);
		}
		
	}
	private class ClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
           switch (v.getId()) {
		case R.id.act_peraon:
			break;

		default:
			break;
		}			
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	

}
