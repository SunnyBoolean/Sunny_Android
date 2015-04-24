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
 * @Description: 天气实体类
 * @date: 2015年3月5日
 */
public class WeatherInfo {
	/** 城市名字 */
	public String cityName;
	/** 日期 */
	public String date;
	/** 星期 */
	public String weekName;
	/** 天气图片 */
	public String weaImageUrl;
	/** 图片Bitmap对象 */
	public Bitmap weatherBitmap;
	/** 最大温度 */
	public String maxTemper;
	/** 最小温度 */
	public String minTemper;
	/** 风速 */
	public String winp;
	/** 天气*/
    public String weather;
    /**
     * 获取天气情况，一周天气列表
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
				// 避免空指针和数组越界
				if (temper != null && temper.length >= 2) {
					wetherInfo.maxTemper = temper[0];
					wetherInfo.minTemper = temper[1];
				}
				//天气预置图片，在真正图片没有加载前ListView上显示的就是此图片
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
	 * 获取天气图片
	 * 
	 * @param imgurl
	 */
	public static Bitmap getWeatherImage(final String imgurl) {
		// 图片处理
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
