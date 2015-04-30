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
 * @Description:  �½�� 
 * @date:  2015��4��20��
 */
public class AddNewEventsActivity extends Activity{
	/** ������*/
	private Context mContext;
    /** �����*/
	private EditText mEventNameEt;
	/** �����ʱ��*/
	private TextView mEventStartTime;
	/** �����ʱ��*/
	private TextView mEventEndTime;
	/** �����*/
	private EditText mEventContent;
	/** ��ص�*/
	private EditText mEventLocation;
	/** �ʵ��*/
	private EventsInfo mEventInfo;
	/** �ϴ�*/
	private Button mComplementBtn;
	/** ʱ��ѡ��Ի���*/
	private Dialog mDateDialog;
	/** ʱ��ѡ�����*/
	private DatePickListener mDatePickListener;
	/** ��ȡ��������*/
	private final Calendar c = Calendar.getInstance();
	private int mYear;
	private int mMonth;
    private int mDay;
    /** �û�ѡ����ǿ�ʼʱ�仹�ǽ���ʱ��*/
    private static int SELECT_DATE;
    /** ��ʼʱ��*/
	private static final int DATE_START_ID = 0;
	/** ����ʱ��*/
	private static final int DATE_ENDT_ID = 1;
	/** ��ǰ�û�*/
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
	 * ��ʼ������ʱ�䣬Ϊ��ǰʱ��
	 */
	private void initCalendar(){
		  mYear = c.get(Calendar.YEAR);
	        mMonth = c.get(Calendar.MONTH);
	        mDay = c.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * ��ȡ����
	 */
	private void initReadInput(){
        if(mEventInfo ==null){
        	mEventInfo = new EventsInfo();
        }
        //��ȡ��ǰ�û�
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
	 * ��������Ƿ�Ϸ������ݲ���Ϊ��
	 */
	private ValidateResult checkInput(){
		ValidateResult result = new ValidateResult();
		if(TextUtils.isEmpty(mEventInfo.eventName)){
			result.isValidate = false;
			result.resultInfo = "����Ʋ���Ϊ��";
		}else if(TextUtils.isEmpty(mEventInfo.eventContent)){
			result.isValidate = false;
			result.resultInfo = "����ݲ���Ϊ��";
		}else if(TextUtils.isEmpty(mEventInfo.eventLocation)){
			result.isValidate = false;
			result.resultInfo="��ص㲻��Ϊ��";
		}else if(TextUtils.isEmpty(mEventInfo.endTime)){
			result.isValidate = false;
			result.resultInfo = "�����ʱ�䲻��Ϊ��";
		}else if(TextUtils.isEmpty(mEventInfo.startTime)){
			result.isValidate = false;
			result.resultInfo = "���ʼʱ�䲻��Ϊ��";
		}else{
			result.isValidate = true;
			result.resultInfo = "��ϴ��ɹ���";
		}
		return result;
	}
	/**
	 * ��ʼ��������
	 */
	private void initListener(){
		//�����¼�����
		ClickListener listener = new ClickListener();
		//ʱ��ѡ�����
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
	  * ʱ��ѡ�����
	  * @author:  liwei
	  * @Description:  TODO 
	  * @date:  2015��4��22��
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
	     * @Description:  �����¼������� 
	     * @date:  2015��4��20��
	     */
	    private class ClickListener implements OnClickListener{
            @Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.evt_complement:
					//��ȡ����
					initReadInput();
					//�����֤
					ValidateResult result = checkInput();
					//�����֤ͨ�����ϴ��������ϴ���������ʾ
					if(result.isValidate){
						//�ϴ�
						HttpResultInfo httpResult = SchoolInfoClient.uploadNewEvent(mEventInfo,mContext);
						//����ϴ��ɹ������ٵ�ǰ����
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
