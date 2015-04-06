/**
 * 
 */
package com.scho.note.client;

/**
 * @author 李伟
 * @Description:客户端请求服务器返回的结果信息
 * @date:2015年4月5日 下午5:37:52
 */

public  class HttpResultInfo {
    /** 是否成功*/
	public boolean success = true;
	/** 返回信息*/
	public String resultInfo;
	/** 返回附加信息*/
	public String attachInfo;
}
