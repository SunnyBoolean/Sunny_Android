/**
 * 
 */
package com.scho.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author liwei
 * @Description:为避免AsyncTask的缺点，而提供的线程池帮助类，
 * @date:2015年2月3日 下午3:01:04
 * 
 *   说明：AsyncTask适合处理短时间的操作，长时间的操作还需要我们自己创建线程来完成
 *   
 *   我们可以通过AsyncTask.execute()方法执行任务时一次执行一条，当前任务执行完才执行下一条，因此不是并发的。
 *   系统默认的线程池用的是SerialExecutor.这个线程池控制所有任务按顺序执行。也就是一次只执行一条.
 *
 *　也可以使用AsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)线程池来处理任务
 *　默认这个线程池是并发处理任务的，也就是不按顺序来.核心为5条,最大128条
 */

public class ThreadUtil {
		
	
	
	/**
	 * 
	 * Description: 获取一个可缓存的线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，
	 *              若无可回收，则新建线程
	 * @author: liwei
	 * @date:2015年2月3日 下午3:09:44
	 * @return
	 */
	public static ExecutorService getNewCachedThreadPool(){
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		return cachedThreadPool;
	}
	/**
	 * 
	 * Description: 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
	 * @author: liwei
	 * @date:2015年2月3日 下午3:13:07
	 * @param num
	 * @return
	 */
	public static ExecutorService getNewFixedThreadPool(int num){
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(num);
        return fixedThreadPool;		
	}
	/**
	 * 
	 * Description: 创建一个定长线程池，支持延迟执行和执行周期
	 * @author: liwei
	 * @date:2015年2月3日 下午3:13:59
	 * @return  返回一个ScheduledExecutorService对象，该对象可以设置延迟时间等操作
	 */
	public static ScheduledExecutorService getNewScheduledThreadPool(int num){
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(num);
        return scheduledThreadPool;		
	}
	/**
	 * 
	 * Description: 获取单线程化的线程池，它只会用唯一的工作线程来执行任务，
	 *              保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
	 * @author: liwei
	 * @date:2015年2月3日 下午3:16:04
	 * @return
	 */
	public static ExecutorService getNewSingleThreadExecutor(){
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        return singleThreadExecutor;		
	}
}
