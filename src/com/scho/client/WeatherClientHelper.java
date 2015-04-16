/**
 * 
 */
package com.scho.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author: hello
 * @Description: 根据api获取天气
 * @date: 2015年3月5日
 */
public class WeatherClientHelper {
	private Context mContext;

	public WeatherClientHelper() {
	}

	public WeatherClientHelper(Context context) {
		mContext = context;
	}

	/**
	 * 根据给定的url地址访问网络，得到响应内容(这里为GET方式访问)
	 * 
	 * @param url
	 *            指定天气url地址
	 * @return 返回结果数据，返回失败时是null
	 */
	public String getWeatherFromServe(String url) {
		// 创建一个http请求对象
		HttpGet request = new HttpGet(url);
		// 创建HttpParams以用来设置HTTP参数
		HttpParams params = new BasicHttpParams();
		// 设置连接超时或响应超时
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSoTimeout(params, 5000);
		// 创建一个网络访问处理对象
		HttpClient httpClient = new DefaultHttpClient(params);
		try {
			// 执行请求参数项
			HttpResponse response = httpClient.execute(request);
			// 判断是否请求成功
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 获得响应信息
				String content = EntityUtils.toString(response.getEntity());
				return content;
			} else {
				// 网连接失败，使用Toast显示提示信息
				Toast.makeText(mContext, "网络访问失败，请检查您机器的联网设备!",
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放网络连接资源
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}

	/**
	 * 从网络获取图片，以输入流的形式返回
	 * 
	 * @return
	 * @throws IOException
	 */
	public static InputStream getImageViewInputStream(String imgurl)
			throws IOException {
		InputStream inputStream = null;
		URL url = new URL(imgurl); // 服务器地址
		if (url != null) {
			// 打开连接
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setConnectTimeout(3000);// 设置网络连接超时的时间为3秒
			httpURLConnection.setRequestMethod("GET"); // 设置请求方法为GET
			httpURLConnection.setDoInput(true); // 打开输入流
			int responseCode = httpURLConnection.getResponseCode(); // 获取服务器响应值
			if (responseCode == HttpURLConnection.HTTP_OK) { // 正常连接
				inputStream = httpURLConnection.getInputStream(); // 获取输入流
			}
		}
		return inputStream;
	}

}
