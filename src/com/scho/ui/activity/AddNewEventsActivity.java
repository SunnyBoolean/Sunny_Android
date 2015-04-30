/**
 * 
 */
package com.scho.ui.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.scho.ValidateResult;
import com.scho.client.HttpResultInfo;
import com.scho.client.SchoolInfoClient;
import com.scho.entity.EventsInfo;
import com.scho.ui.R;

/**
 * @author:  liwei
 * @Description:  新建活动 
 * @date:  2015年4月20日
 */
public class AddNewEventsActivity extends Activity{
	/** 上下文*/
	private Context mContext;
    /** 活动名称*/
	private EditText mEventNameEt;
	/** 活动发起时间*/
	private TextView mEventStartTime;
	/** 活动结束时间*/
	private TextView mEventEndTime;
	/** 活动内容*/
	private EditText mEventContent;
	/** 活动地点*/
	private EditText mEventLocation;
	/** 活动实体*/
	private EventsInfo mEventInfo;
	/** 上传*/
	private Button mComplementBtn;
	/** 时间选择对话框*/
	private Dialog mDateDialog;
	/** 时间选择监听*/
	private DatePickListener mDatePickListener;
	/** 获取日历对象*/
	private final Calendar c = Calendar.getInstance();
	private int mYear;
	private int mMonth;
    private int mDay;
    /** 用户选择的是开始时间还是结束时间*/
    private static int SELECT_DATE;
    /** 开始时间*/
	private static final int DATE_START_ID = 0;
	/** 结束时间*/
	private static final int DATE_ENDT_ID = 1;
	/** 当前用户*/
	private AVUser mCurUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.add_new_events);
		mEventNameEt = (EditText) findViewById(R.id.evt_name);
		mEventEndTime = (TextView) findViewById(R.id.evt_endtime);
		mEventLocation = (EditText) findViewById(R.id.evt_location);
		mEventStartTime = (TextView) findViewById(R.id.evt_starttime);
		mEventContent = (EditText) findViewById(R.id.evt_content);
		mComplementBtn = (Button) findViewById(R.id.evt_complement);
		initCalendar();
		initListener();
	    updateDisplay(DATE_START_ID);
	    mCurUser = AVUser.getCurrentUser();
	}
	/**
	 * 初始化日历时间，为当前时间
	 */
	private void initCalendar(){
		  mYear = c.get(Calendar.YEAR);
	        mMonth = c.get(Calendar.MONTH);
	        mDay = c.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * 读取内容
	 */
	private void initReadInput(){
        if(mEventInfo ==null){
        	mEventInfo = new EventsInfo();
        }
        //获取当前用户
        AVUser user = AVUser.getCurrentUser();
       
        mEventInfo.eventName = mEventNameEt.getText().toString();
        mEventInfo.eventContent = mEventContent.getText().toString();
        mEventInfo.eventLocation = mEventLocation.getText().toString();
        if(user !=null){
        	mEventInfo.personName = user.getUsername();
        }else{
        	mEventInfo.personName = user.getUsername();
        }
        mEventInfo.endTime = mEventEndTime.getText().toString();
        mEventInfo.startTime = mEventStartTime.getText().toString();
	}
	/**
	 * 检查输入是否合法，内容不能为空
	 */
	private ValidateResult checkInput(){
		ValidateResult result = new ValidateResult();
		if(TextUtils.isEmpty(mEventInfo.eventName)){
			result.isValidate = false;
			result.resultInfo = "活动名称不能为空";
		}else if(TextUtils.isEmpty(mEventInfo.eventContent)){
			result.isValidate = false;
			result.resultInfo = "活动内容不能为空";
		}else if(TextUtils.isEmpty(mEventInfo.eventLocation)){
			result.isValidate = false;
			result.resultInfo="活动地点不能为空";
		}else if(TextUtils.isEmpty(mEventInfo.endTime)){
			result.isValidate = false;
			result.resultInfo = "活动结束时间不能为空";
		}else if(TextUtils.isEmpty(mEventInfo.startTime)){
			result.isValidate = false;
			result.resultInfo = "活动开始时间不能为空";
		}else{
			result.isValidate = true;
			result.resultInfo = "活动上传成功！";
		}
		return result;
	}
	/**
	 * 初始化监听器
	 */
	private void initListener(){
		//单击事件监听
		ClickListener listener = new ClickListener();
		//时间选择监听
		mDatePickListener = new DatePickListener();
		mEventStartTime.setOnClickListener(listener);
		mEventEndTime.setOnClickListener(listener);
		mComplementBtn.setOnClickListener(listener);
		mDateDialog = 	new DatePickerDialog(this, mDatePickListener, mYear, mMonth,
                mDay);	
	}
	
	 private void updateDisplay(int id) {
		 switch(id){
		 case DATE_START_ID:
			 mEventStartTime.setText(new StringBuilder().append(mYear).append("-")
	                .append(mMonth + 1).append("-").append(mDay).append(" "));
			 break;
		 case DATE_ENDT_ID:
			 mEventEndTime.setText(new StringBuilder().append(mYear).append("-")
		                .append(mMonth + 1).append("-").append(mDay).append(" "));
			 break;
		 }
	    }
	 /**
	  * 时间选择监听
	  * @author:  liwei
	  * @Description:  TODO 
	  * @date:  2015年4月22日
	  */
	 private class DatePickListener implements DatePickerDialog.OnDateSetListener{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(SELECT_DATE);
		}
		 
	 }
	    /**
	     * 
	     * @author:  liwei
	     * @Description:  单击事件监听器 
	     * @date:  2015年4月20日
	     */
	    private class ClickListener implements OnClickListener{
            @Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.evt_complement:
					//读取内容
					initReadInput();
					//检查验证
					ValidateResult result = checkInput();
					//如果验证通过就上传，否则不上传并给出提示
					if(result.isValidate){
						//上传
						HttpResultInfo httpResult = SchoolInfoClient.uploadNewEvent(mEventInfo,mContext);
						//如果上传成功就销毁当前界面
						if(httpResult.success){
							setResult(13);
							finish();
						}
						
					}else{
						Toast.makeText(mContext, result.resultInfo, Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.evt_starttime:
					SELECT_DATE = DATE_START_ID;
					mDateDialog.show();
					break;
				case R.id.evt_endtime:
					SELECT_DATE = DATE_ENDT_ID;
					mDateDialog.show();
					break;
				default:
					break;
				}
			}
	    	
	    }
    
}
