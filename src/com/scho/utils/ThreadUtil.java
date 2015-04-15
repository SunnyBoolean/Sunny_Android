/**
 * 
 */
package com.scho.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author liwei
 * @Description:Ϊ����AsyncTask��ȱ�㣬���ṩ���̳߳ذ����࣬
 * @date:2015��2��3�� ����3:01:04
 * 
 *   ˵����AsyncTask�ʺϴ����ʱ��Ĳ�������ʱ��Ĳ�������Ҫ�����Լ������߳������
 *   
 *   ���ǿ���ͨ��AsyncTask.execute()����ִ������ʱһ��ִ��һ������ǰ����ִ�����ִ����һ������˲��ǲ����ġ�
 *   ϵͳĬ�ϵ��̳߳��õ���SerialExecutor.����̳߳ؿ�����������˳��ִ�С�Ҳ����һ��ִֻ��һ��.
 *
 *��Ҳ����ʹ��AsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)�̳߳�����������
 *��Ĭ������̳߳��ǲ�����������ģ�Ҳ���ǲ���˳����.����Ϊ5��,���128��
 */

public class ThreadUtil {
		
	
	
	/**
	 * 
	 * Description: ��ȡһ���ɻ�����̳߳أ�����̳߳س��ȳ���������Ҫ���������տ����̣߳�
	 *              ���޿ɻ��գ����½��߳�
	 * @author: liwei
	 * @date:2015��2��3�� ����3:09:44
	 * @return
	 */
	public static ExecutorService getNewCachedThreadPool(){
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		return cachedThreadPool;
	}
	/**
	 * 
	 * Description: ����һ�������̳߳أ��ɿ����߳���󲢷������������̻߳��ڶ����еȴ�
	 * @author: liwei
	 * @date:2015��2��3�� ����3:13:07
	 * @param num
	 * @return
	 */
	public static ExecutorService getNewFixedThreadPool(int num){
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(num);
        return fixedThreadPool;		
	}
	/**
	 * 
	 * Description: ����һ�������̳߳أ�֧���ӳ�ִ�к�ִ������
	 * @author: liwei
	 * @date:2015��2��3�� ����3:13:59
	 * @return  ����һ��ScheduledExecutorService���󣬸ö�����������ӳ�ʱ��Ȳ���
	 */
	public static ScheduledExecutorService getNewScheduledThreadPool(int num){
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(num);
        return scheduledThreadPool;		
	}
	/**
	 * 
	 * Description: ��ȡ���̻߳����̳߳أ���ֻ����Ψһ�Ĺ����߳���ִ������
	 *              ��֤����������ָ��˳��(FIFO, LIFO, ���ȼ�)ִ��
	 * @author: liwei
	 * @date:2015��2��3�� ����3:16:04
	 * @return
	 */
	public static ExecutorService getNewSingleThreadExecutor(){
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        return singleThreadExecutor;		
	}
}
