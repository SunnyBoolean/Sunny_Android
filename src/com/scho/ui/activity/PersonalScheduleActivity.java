
package com.scho.ui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.scho.ui.R;
import com.scho.widget.CalendarView;
import com.scho.widget.CalendarView.OnItemClickListener;

/**
 * @author:  liwei
 * @Description:  �����ճ̰��� 
 * @date:  2015��4��20��
 */
public class PersonalScheduleActivity extends Activity {
	/** ������*/
	private Context mContext;
	private CalendarView calendar;
	private ImageButton calendarLeft;
	private TextView calendarCenter;
	private ImageButton calendarRight;
	private SimpleDateFormat format;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_schedule);
         mContext = this;
		format = new SimpleDateFormat("yyyy-MM-dd");
		//��ȡ�����ؼ�����
		calendar = (CalendarView)findViewById(R.id.calendar);
		calendar.setSelectMore(false); //��ѡ  
		
		
		calendarLeft = (ImageButton)findViewById(R.id.calendarLeft);
		calendarCenter = (TextView)findViewById(R.id.calendarCenter);
		calendarRight = (ImageButton)findViewById(R.id.calendarRight);
		try {
			//������������
			Date date = format.parse("2015-01-01");
			calendar.setCalendarData(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//��ȡ���������� ya[0]Ϊ�꣬ya[1]Ϊ�£���ʽ��ҿ��������������ؼ��иģ�
		String[] ya = calendar.getYearAndmonth().split("-"); 
		calendarCenter.setText(ya[0]+"��"+ya[1]+"��");
		calendarLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�����һ�� ͬ���������� 
				String leftYearAndmonth = calendar.clickLeftMonth(); 
				String[] ya = leftYearAndmonth.split("-"); 
				calendarCenter.setText(ya[0]+"��"+ya[1]+"��");
			}
		});
		
		calendarRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�����һ��
				String rightYearAndmonth = calendar.clickRightMonth();
				String[] ya = rightYearAndmonth.split("-"); 
				calendarCenter.setText(ya[0]+"��"+ya[1]+"��");
			}
		});
		//���ÿؼ����������Լ����������ÿһ�죨���Ҳ�����ڿؼ��и��������趨��
		calendar.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void OnItemClick(Date selectedStartDate,
					Date selectedEndDate, Date downDate) {
				format.format(downDate);
				Intent intent = new Intent();
				intent.setClass(mContext, AddNewScheduleActivity.class);
//				startActivityForResult(intent, 21);
				calendar.setItem(5);
			}
		});
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==21 && resultCode ==22 && data !=null) {
			//�����ĳһ����������ӹ��ճ̰��ţ������ػ�����
			boolean isSuccess = data.getBooleanExtra("isSuccess", false);
			if(isSuccess){
//				calendar.postInvalidate();
				calendar.setItem(5);
			}
		}
		
    }
}
