package com.scho.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Base64;

/**
 * 
 * @author liwei
 * @Description: 对图片以及控件的操作辅助类，如改变控件大小、位置 。以及图片的缩放、裁剪等
 * @date:2015年2月3日 上午10:55:25
 */
public class ImageTool {
	/**
	 * 
	 * Description: 图片的缩放
	 * 
	 * @author: liwei
	 * @date:2015年2月3日 上午10:57:42
	 * @param bm
	 *            原图Bitmap对象
	 * @param newWidth
	 *            宽度，如果在0~1之间就是放大newWidth倍，如果大于1就是缩小到原来的1/newWidth
	 * @param newHeight
	 *            高度，如果在0~1之间就是放大newWidth倍，如果大于1就是缩小到原来的1/newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	/**
	 * 
	 * Description: 判断有无网络连接
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午2:06:40
	 * @param mContext
	 * @return
	 */
	public static boolean checkNetworkInfo(Context mContext) {
		ConnectivityManager conMan = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			return true;
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return true;
		return false;
	}

	/**
	 * 
	 * Description: 通过路径生成Base64字符串
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午2:05:48
	 * @param path
	 * @return
	 */
	public static String getBase64FromPath(String path) {
		String base64 = "";
		try {
			File file = new File(path);
			byte[] buffer = new byte[(int) file.length() + 100];
			@SuppressWarnings("resource")
			int length = new FileInputStream(file).read(buffer);
			base64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64;
	}

	/**
	 * 
	 * Description: 通过图片的文件路径获取Bitmap，
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午2:03:08
	 * @param path
	 *            文件的全路径
	 * @param w
	 *            控制图片的缩放
	 * @param h
	 * @return
	 */
	public static Bitmap getBitmapFromPath(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小,不会将整个图片获取到，可以防止OOM
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空，此时由于设置了opts.inJustDecodeBounds为ture，即只解码了图片的高和宽，没有生成Bitmap对象
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		//如果scale>1那么获取的图片的高将会是原来的1/scale，宽也是原来的1/scale，同时像素是其1/(scale*4)
		//如果scale<1都会被视为1，即不进行缩小处理
		opts.inSampleSize = (int) scale;
		// 构造若引用对象，节省内存，防止内存泄露
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	/**
	 * 
	 * Description: 从res目录下的资源获取一个Bitmap对象，所获取的图片只会比原图要小并且像素比原图低
	 *              至于低多少取决于w和h，不管w和h值为多少获取的图片的质量都不会比原图要高
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午2:16:23
	 * @param resourceObj
	 *            Resources实例
	 * @param sourceId
	 *            图片id，R.id...
	 * @param w
	 *            宽，用于缩放
	 * @param h
	 *            高，用于缩放
	 * @return
	 */
	public static Bitmap getBitmapFromResourceID(Resources resourceObj,
			int sourceId, int w, int h) {
		Bitmap bitMap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 设置为ture只获取图片大小,不会将整个图片获取到，可以防止OOM
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空，此时由于设置了opts.inJustDecodeBounds为ture，即只解码了图片的高和宽，没有生成Bitmap对象
		BitmapFactory.decodeResource(resourceObj, sourceId,options);
		int width = options.outWidth;   //1920
		int height = options.outHeight; //1080
		
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		options.inJustDecodeBounds = false;
		//总是取其最大，
		float scale = Math.max(scaleWidth, scaleHeight);
		//如果scale>1那么获取的图片的高将会是原来的1/scale，宽也是原来的1/scale，同时像素是其1/(scale*4)
		//如果scale<1都会被视为1，即不进行缩小处理
		options.inSampleSize = (int) scale;
		// 构造若引用对象，节省内存，防止内存泄露
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeResource(resourceObj, sourceId, options));
		bitMap = Bitmap.createScaledBitmap(weak.get(), w, h, true);
		return bitMap;
	}

	/**
	 * 
	 * Description: 将Bitmap转换为Base64字符串
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午2:02:25
	 * @param bitmap
	 * @param bitmapQuality
	 * @return
	 */
	public static String getBase64FromBitmap(Bitmap bitmap, int bitmapQuality) {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, bitmapQuality, bStream);
		byte[] bytes = bStream.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	/**
	 * Description: 将Base64字符串转换为Bitmap
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午2:01:06
	 * @param string
	 * @return
	 */
	public static Bitmap getBitmapFromBase64(String string) {
		byte[] bitmapArray = null;
		try {
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BitmapFactory
				.decodeByteArray(bitmapArray, 0, bitmapArray.length);
	}

	/**
	 * 
	 * Description: Bitmap转换为Drawable
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午2:38:50
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitMapToDrawable(Bitmap bitmap) {
		Drawable drawable = null;
		drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	/**
	 * 
	 * Description: Drawable转换为Bitmap
	 * 
	 * @author: liwei
	 * @date:2015年2月12日 下午2:45:58
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = null;
		if (null != drawable) {
			bitmap = ((BitmapDrawable) drawable).getBitmap();
		}
		return bitmap;
	}

}