/**
 * 
 */
package com.scho.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scho.client.WeatherClientHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * @author: hello
 * @Description: ����ʵ����
 * @date: 2015��3��5��
 */
public class WeatherInfo {
	/** �������� */
	public String cityName;
	/** ���� */
	public String date;
	/** ���� */
	public String weekName;
	/** ����ͼƬ */
	public String weaImageUrl;
	/** ͼƬBitmap���� */
	public Bitmap weatherBitmap;
	/** ����¶� */
	public String maxTemper;
	/** ��С�¶� */
	public String minTemper;
	/** ���� */
	public String winp;
	/** ����*/
    public String weather;
    /**
     * ��ȡ���������һ�������б�
     * @param jsonStr
     * @param context
     * @return
     */
	public static List<WeatherInfo> jsonToWeatherInfo(String jsonStr,Context context) {
		WeatherInfo wetherInfo = null;
		List<WeatherInfo> weatherList = new ArrayList<WeatherInfo>();

		JSONObject jsonItem;
		try {
			jsonItem = new JSONObject(jsonStr);
			JSONArray jsonArr = jsonItem.getJSONArray("result");
			jsonArr.length();
			for (int i = 0; i < jsonArr.length(); i++) {
				wetherInfo = new WeatherInfo();
				JSONObject jsonI = jsonArr.getJSONObject(i);
				if (jsonI != null)
					wetherInfo.weekName = jsonI.optString("week");
				String[] temper = jsonI.optString("temperature").split("/");
				// �����ָ�������Խ��
				if (temper != null && temper.length >= 2) {
					wetherInfo.maxTemper = temper[0];
					wetherInfo.minTemper = temper[1];
				}
				//����Ԥ��ͼƬ��������ͼƬû�м���ǰListView����ʾ�ľ��Ǵ�ͼƬ
//				wetherInfo.weatherBitmap = ImageTool.getBitmapFromResourceID(context.getResources(), R.drawable.alerts_red_27, 50, 50);
				wetherInfo.weather = jsonI.optString("weather");
				wetherInfo.weaImageUrl = jsonI.optString("weather_icon");
				wetherInfo.date = jsonI.optString("days");
				wetherInfo.cityName = jsonI.optString("citynm");
				wetherInfo.winp = jsonI.optString("winp");
				weatherList.add(wetherInfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return weatherList;
	}

	/**
	 * ��ȡ����ͼƬ
	 * 
	 * @param imgurl
	 */
	public static Bitmap getWeatherImage(final String imgurl) {
		// ͼƬ����
		InputStream inputStream = null;
		Bitmap bitmap = null;
		try {
			inputStream = WeatherClientHelper.getImageViewInputStream(imgurl);
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
}
