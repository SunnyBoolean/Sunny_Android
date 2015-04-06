/**
 * 
 */
package com.scho.note.client;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.scho.http.BasicNameValuePair;
import com.scho.http.HttpRequest;
import com.scho.note.Constants;
import com.scho.note.entity.NoteInfo;
import com.scho.note.entity.UserInfo;

/**
 * @author 李伟
 * @Description:用户信息服务器交互
 * @date:2015年3月10日 下午8:31:38
 */

public class UserInfoClient extends BaseHelperClient{
    /**
     * 
     * Description: 获取当前账号的基本信息，包括头像、昵称、性别、年龄等
     * @author: 李伟
     * @date:2015年4月3日 上午1:31:14
     * @param url  请求接口地址
     * @param mesList  请求参数
     * @param methodType  请求方法  get
     * @return
     */
	public static UserInfo getUserBaseInfo(String url,List<BasicNameValuePair> mesList,String methodType){
		String userMes = "";
		InputStream is = null;
		UserInfo userInfo = new UserInfo();
		try {
			is = new HttpRequest().httpRequest(url, mesList, null, true, "GET");
			userMes = inStreamToString(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// ====== json字符串解析  ======
		
		JSONArray jsonArr = null;
		try {
			jsonArr = new JSONArray(userMes);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<jsonArr.length();i++){
			JSONObject jsonO;
			try {
				jsonO = jsonArr.getJSONObject(i);
				userInfo.sinaUserName = jsonO.getString("screen_name");
				userInfo.sinaUserImageUrl = jsonO.getString("profile_image_url");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		return userInfo;
	}
	

}
