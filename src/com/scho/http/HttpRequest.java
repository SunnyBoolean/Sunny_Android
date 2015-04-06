package com.scho.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HttpRequest {

	/** 未指定 */
	public static final int NONE = 0;

	/** OK: Success! */
	public static final int OK = 200;
	/** Not Modified: There was no new data to return. */
	public static final int NOT_MODIFIED = 304;
	/**
	 * Bad Request: The request was invalid. An accompanying error message will
	 * explain why. This is the status code will be returned during rate
	 * limiting.
	 */
	public static final int BAD_REQUEST = 400;
	/** Not Authorized: Authentication credentials were missing or incorrect. */
	public static final int NOT_AUTHORIZED = 401;
	/**
	 * Forbidden: The request is understood, but it has been refused. An
	 * accompanying error message will explain why.
	 */
	public static final int FORBIDDEN = 403;
	/**
	 * Not Found: The URI requested is invalid or the resource requested, such
	 * as a user, does not exists.
	 */
	public static final int NOT_FOUND = 404;
	/**
	 * Not Acceptable: Returned by the Search API when an invalid format is
	 * specified in the request.
	 */
	public static final int NOT_ACCEPTABLE = 406;
	/**
	 * Internal Server Error: Something is broken. Please post to the group so
	 * the Weibo team can investigate.
	 */
	public static final int INTERNAL_SERVER_ERROR = 500;
	/** Bad Gateway: Weibo is down or being upgraded. */
	public static final int BAD_GATEWAY = 502;
	/**
	 * Service Unavailable: The Weibo servers are up, but overloaded with
	 * requests. Try again later. The search and trend methods use this to
	 * indicate when you are being rate limited.
	 */
	public static final int SERVICE_UNAVAILABLE = 503;

	public static final int RETRIEVE_LIMIT = 20;
	public static final int RETRIED_TIME = 3;

	public int response = HttpRequest.OK;

	private int timeout = -1;

	public void setTimeOut(int timeout) {
		this.timeout = timeout;
	}

	private final Set<OnHttpListener> _listeners = new HashSet<OnHttpListener>();

	public void addHttpListener(OnHttpListener listener) {
		if (!this._listeners.contains(listener)) {
			this._listeners.add(listener);
		}
	}

	public void removeHttpListener(OnHttpListener listener) {
		if (this._listeners.contains(listener)) {
			this._listeners.remove(listener);
		}
	}

	public InputStream httpRequest(String url,
			List<BasicNameValuePair> paras, ArrayList<File> files,
			boolean authenticated, String httpMethod) throws IOException {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		HttpURLConnection con = null;
		DataOutputStream ds = null;
		InputStream is = null;
		int responseCode = -1;
		String error = "";
		Iterator<OnHttpListener> it = this._listeners.iterator();
		try {
			/* 显示进度 */
			while (it.hasNext()) {
				OnHttpListener listener = it.next();
				listener.OnStart();
			}

			if (httpMethod.equals("GET") && null != paras) {
				if (!url.contains("?")){
					url += "?";
				}
				for (BasicNameValuePair kv : paras) {
					url += ("&" + kv.key + "=" + kv.value);
				}				
			}
			
			URL uri = new URL(url);
			con = (HttpURLConnection) uri.openConnection();
			if (this.timeout > 0) {
				con.setConnectTimeout(this.timeout);
			} else {
				con.setConnectTimeout(30000);
			}
			// con.setReadTimeout(60000);
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");

			if (httpMethod.equals("POST")) {
				/* 设定传送的method=POST */
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				/* 设定DataOutputStream */
				ds = new DataOutputStream(con.getOutputStream());

				/* 添加键值 */
				if (null != paras) {
					for (BasicNameValuePair kv : paras) {
						StringBuilder sb = new StringBuilder();
						sb.append(end + twoHyphens + boundary + end);
						sb.append("Content-Disposition: form-data; "
								+ "name=\"" + kv.key + "\"" + end + end);
						sb.append((null==kv.value)?"":kv.value);
						// sb.append(twoHyphens + boundary);
						byte[] bytes = sb.toString().getBytes("utf8");
						ds.write(bytes);
					}
					ds.write((end + twoHyphens + boundary + end)
							.getBytes("utf8"));
				}
				/* 发送文件 */
				if (null != files) {
					for (File file : files) {
						ds.writeBytes(twoHyphens + boundary + end);
						ds.writeBytes("Content-Disposition: form-data; "
								+ "name=\"" + file.getName() + "\";filename=\""
								+ file.getName() + "\"" + end + end);

						FileInputStream fStream = new FileInputStream(file);
						String fileName = file.getName();
						int bufferSize = 1024;
						byte[] buffer = new byte[bufferSize];

						int length = -1;
						long total = 0;
						long fileLength = file.length();
						while ((length = fStream.read(buffer)) != -1) {
							ds.write(buffer, 0, length);
							total += length;

							it = this._listeners.iterator();
							while (it.hasNext()) {
								OnHttpListener listener = it.next();
								listener.OnProgress(fileName, total,
										(int) (100 * total / fileLength));
							}
						}
						ds.write((end + twoHyphens + boundary + end)
								.getBytes("utf8"));
						fStream.close();
					}
					/* 所有文件上传完成后加上这一行 */
					ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
					ds.flush();
				}
			} else if (httpMethod.equals("GET")) {
				con.setRequestMethod("GET");
			}

			/* 等待返回结果 */
			it = this._listeners.iterator();
			while (it.hasNext()) {
				OnHttpListener listener = it.next();
				listener.OnWaitResponse();
			}

			responseCode = con.getResponseCode();
			this.response = responseCode;
			/* 取得Response内容 */
			is = con.getInputStream();

			/* 显示进度 */
			it = this._listeners.iterator();
			while (it.hasNext()) {
				OnHttpListener listener = it.next();
				listener.OnResponsed(is);
			}

		} catch (MalformedURLException e) {
			responseCode = -2;
			error = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			responseCode = -3;
			error = e.getMessage();
			e.printStackTrace();
		} catch (Exception e) {
			responseCode = -4;
			error = e.getMessage();
			e.printStackTrace();
		} finally {
			if (null != ds) {
				ds.close();
				ds = null;
			}
			if (responseCode == HttpRequest.OK) {
				/* 显示进度 */
				it = this._listeners.iterator();
				while (it.hasNext()) {
					OnHttpListener listener = it.next();
					listener.OnEnd(true);
				}

			} else {
				/* 显示进度 */
				it = this._listeners.iterator();
				while (it.hasNext()) {
					OnHttpListener listener = it.next();
					listener.OnError(responseCode, error);
				}
			}
		}

		return is;
	}

}
