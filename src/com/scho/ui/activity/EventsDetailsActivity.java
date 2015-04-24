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
 * @Description:  校园活动详情 
 * @date:  2015年4月17日
 */
public class EventsDetailsActivity extends Activity{
	/** 活动发起人*/
	private TextView mEventPerson;
	/** 活动开始时间*/
	private TextView mEventStartTime;
	/** 活动结束时间*/
	private TextView mEventEndTime;
	/** 活动内容*/
	private TextView mEventContent;
	/** 活动地点吧*/
	private TextView mEventLocation;
	/** ActionBar*/
    private ActionBar mActionBar;
    /** 活动实体类*/
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
	 * 初始化去ActionBar
	 */
	public void initActionBar(){
		mActionBar = getActionBar();
		mActionBar.setTitle("活动详情");
		mActionBar.setIcon(R.drawable.ic_pre);
		mActionBar.setHomeButtonEnabled(true);
	}
	/**
	 * 初始化内容
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
