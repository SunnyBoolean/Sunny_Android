package com.scho.http;

import java.util.ArrayList;

public class KeyValueCollection extends ArrayList<BasicNameValuePair> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void add(String key, boolean value){
		super.add(new BasicNameValuePair(key, value?"true":"false"));
	}
	
	public void add(String key, int value){
		super.add(new BasicNameValuePair(key, String.valueOf(value)));
	}
	
	public void add(String key, long value){
		super.add(new BasicNameValuePair(key, String.valueOf(value)));
	}
	
	public void add(String key, String value){
		super.add(new BasicNameValuePair(key, (null==value?"":value)));
	}
	
	public void add(String key, double value){
		super.add(new BasicNameValuePair(key, String.valueOf(value)));
	}
	
	public void add(String key, Object value){
		super.add(new BasicNameValuePair(key, String.valueOf(value)));
	}
	
}
