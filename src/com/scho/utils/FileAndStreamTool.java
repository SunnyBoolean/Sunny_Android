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
 * @Description:文件与多种输入、输出流的转换，以及文件的解码与编码的Nase64字符串之间的转换
 * @date:2015年2月12日 下午2:21:57
 */

public class FileAndStreamTool {

	/**
	 * 
	 * Description: 将InputStream流转换成String
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午1:57:16
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
	 * Description: 从路径获取文件名
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午2:06:13
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
