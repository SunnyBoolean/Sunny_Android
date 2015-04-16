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
 * @Description: ����api��ȡ����
 * @date: 2015��3��5��
 */
public class WeatherClientHelper {
	private Context mContext;

	public WeatherClientHelper() {
	}

	public WeatherClientHelper(Context context) {
		mContext = context;
	}

	/**
	 * ���ݸ�����url��ַ�������磬�õ���Ӧ����(����ΪGET��ʽ����)
	 * 
	 * @param url
	 *            ָ������url��ַ
	 * @return ���ؽ�����ݣ�����ʧ��ʱ��null
	 */
	public String getWeatherFromServe(String url) {
		// ����һ��http�������
		HttpGet request = new HttpGet(url);
		// ����HttpParams����������HTTP����
		HttpParams params = new BasicHttpParams();
		// �������ӳ�ʱ����Ӧ��ʱ
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSoTimeout(params, 5000);
		// ����һ��������ʴ������
		HttpClient httpClient = new DefaultHttpClient(params);
		try {
			// ִ�����������
			HttpResponse response = httpClient.execute(request);
			// �ж��Ƿ�����ɹ�
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// �����Ӧ��Ϣ
				String content = EntityUtils.toString(response.getEntity());
				return content;
			} else {
				// ������ʧ�ܣ�ʹ��Toast��ʾ��ʾ��Ϣ
				Toast.makeText(mContext, "�������ʧ�ܣ������������������豸!",
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// �ͷ�����������Դ
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}

	/**
	 * �������ȡͼƬ��������������ʽ����
	 * 
	 * @return
	 * @throws IOException
	 */
	public static InputStream getImageViewInputStream(String imgurl)
			throws IOException {
		InputStream inputStream = null;
		URL url = new URL(imgurl); // ��������ַ
		if (url != null) {
			// ������
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setConnectTimeout(3000);// �����������ӳ�ʱ��ʱ��Ϊ3��
			httpURLConnection.setRequestMethod("GET"); // �������󷽷�ΪGET
			httpURLConnection.setDoInput(true); // ��������
			int responseCode = httpURLConnection.getResponseCode(); // ��ȡ��������Ӧֵ
			if (responseCode == HttpURLConnection.HTTP_OK) { // ��������
				inputStream = httpURLConnection.getInputStream(); // ��ȡ������
			}
		}
		return inputStream;
	}

}
