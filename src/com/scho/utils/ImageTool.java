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
 * @Description: ��ͼƬ�Լ��ؼ��Ĳ��������࣬��ı�ؼ���С��λ�� ���Լ�ͼƬ�����š��ü���
 * @date:2015��2��3�� ����10:55:25
 */
public class ImageTool {
	/**
	 * 
	 * Description: ͼƬ������
	 * 
	 * @author: liwei
	 * @date:2015��2��3�� ����10:57:42
	 * @param bm
	 *            ԭͼBitmap����
	 * @param newWidth
	 *            ��ȣ������0~1֮����ǷŴ�newWidth�����������1������С��ԭ����1/newWidth
	 * @param newHeight
	 *            �߶ȣ������0~1֮����ǷŴ�newWidth�����������1������С��ԭ����1/newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// ���ͼƬ�Ŀ��
		int width = bm.getWidth();
		int height = bm.getHeight();
		// �������ű���
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// �õ��µ�ͼƬ
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	/**
	 * 
	 * Description: �ж�������������
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����2:06:40
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
	 * Description: ͨ��·������Base64�ַ���
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����2:05:48
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
	 * Description: ͨ��ͼƬ���ļ�·����ȡBitmap��
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����2:03:08
	 * @param path
	 *            �ļ���ȫ·��
	 * @param w
	 *            ����ͼƬ������
	 * @param h
	 * @return
	 */
	public static Bitmap getBitmapFromPath(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// ����Ϊtureֻ��ȡͼƬ��С,���Ὣ����ͼƬ��ȡ�������Է�ֹOOM
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// ����Ϊ�գ���ʱ����������opts.inJustDecodeBoundsΪture����ֻ������ͼƬ�ĸߺͿ�û������Bitmap����
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// ����
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		//���scale>1��ô��ȡ��ͼƬ�ĸ߽�����ԭ����1/scale����Ҳ��ԭ����1/scale��ͬʱ��������1/(scale*4)
		//���scale<1���ᱻ��Ϊ1������������С����
		opts.inSampleSize = (int) scale;
		// ���������ö��󣬽�ʡ�ڴ棬��ֹ�ڴ�й¶
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	/**
	 * 
	 * Description: ��resĿ¼�µ���Դ��ȡһ��Bitmap��������ȡ��ͼƬֻ���ԭͼҪС�������ر�ԭͼ��
	 *              ���ڵͶ���ȡ����w��h������w��hֵΪ���ٻ�ȡ��ͼƬ�������������ԭͼҪ��
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����2:16:23
	 * @param resourceObj
	 *            Resourcesʵ��
	 * @param sourceId
	 *            ͼƬid��R.id...
	 * @param w
	 *            ����������
	 * @param h
	 *            �ߣ���������
	 * @return
	 */
	public static Bitmap getBitmapFromResourceID(Resources resourceObj,
			int sourceId, int w, int h) {
		Bitmap bitMap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		// ����Ϊtureֻ��ȡͼƬ��С,���Ὣ����ͼƬ��ȡ�������Է�ֹOOM
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// ����Ϊ�գ���ʱ����������opts.inJustDecodeBoundsΪture����ֻ������ͼƬ�ĸߺͿ�û������Bitmap����
		BitmapFactory.decodeResource(resourceObj, sourceId,options);
		int width = options.outWidth;   //1920
		int height = options.outHeight; //1080
		
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// ����
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		options.inJustDecodeBounds = false;
		//����ȡ�����
		float scale = Math.max(scaleWidth, scaleHeight);
		//���scale>1��ô��ȡ��ͼƬ�ĸ߽�����ԭ����1/scale����Ҳ��ԭ����1/scale��ͬʱ��������1/(scale*4)
		//���scale<1���ᱻ��Ϊ1������������С����
		options.inSampleSize = (int) scale;
		// ���������ö��󣬽�ʡ�ڴ棬��ֹ�ڴ�й¶
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeResource(resourceObj, sourceId, options));
		bitMap = Bitmap.createScaledBitmap(weak.get(), w, h, true);
		return bitMap;
	}

	/**
	 * 
	 * Description: ��Bitmapת��ΪBase64�ַ���
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����2:02:25
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
	 * Description: ��Base64�ַ���ת��ΪBitmap
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����2:01:06
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
	 * Description: Bitmapת��ΪDrawable
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����2:38:50
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
	 * Description: Drawableת��ΪBitmap
	 * 
	 * @author: liwei
	 * @date:2015��2��12�� ����2:45:58
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