/**
 * 
 */
package com.scho.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author liwei
 * @Description:�ļ���������롢�������ת�����Լ��ļ��Ľ���������Nase64�ַ���֮���ת��
 * @date:2015��2��12�� ����2:21:57
 */

public class FileAndStreamTool {

	/**
	 * 
	 * Description: ��InputStream��ת����String
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����1:57:16
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * Description: ��·����ȡ�ļ���
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����2:06:13
	 * @param pathandname
	 * @return
	 */
	public static String getFileName(String pathandname) {
		int start = pathandname.lastIndexOf("/");
		int end = pathandname.lastIndexOf(".");
		if (start != -1 && end != -1) {
			return pathandname.substring(start + 1, end);
		} else {
			return null;
		}
	}
}
