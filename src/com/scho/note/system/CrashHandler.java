package com.scho.note.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.TreeSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * @ClassName: CrashHandler
 * @Description: 系统异常处理
 * @author duanchunxian
 * @date 2012-11-16 下午5:14:17
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
    /** @Fields TAG : Debug Log tag */
    private static final String TAG = "CrashHandler";
    /** @Fields LOG_FILE_DIR : 日志文件�? mnt/sdcard/data/sms/log/ */
//    private static final String LOG_FILE_DIR = Constants.SMS_FILE_DIR
//            + File.separator + "log" + File.separator;
    /** @Fields DEBUG : 是否�?启日志输�?,在Debug状�?�下�?�?, 在Release状�?�下关闭以提示程序�?�能 */
//    private static final boolean DEBUG = Constants.isReleaseLogin;
    /** 系统默认的UncaughtException处理�? */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /** CrashHandler实例 */
    private static CrashHandler instance;
    /** 程序的Context对象 */
    private Context mContext;

    /** 使用Properties来保存设备的信息和错误堆栈信�? */
    private final Properties mDeviceCrashInfo = new Properties();

    /** @Fields mSettings : 系统配置 */
    private SharedPreferences mSettings;
    /** @Fields VERSION_NAME : 版本名称 */
    private static final String VERSION_NAME = "versionName";
    /** @Fields VERSION_CODE : 版本编号 */
    private static final String VERSION_CODE = "versionCode";
    /** @Fields STACK_TRACE : 跟踪日志 */
    private static final String STACK_TRACE = "STACK_TRACE";
    /** 错误报告文件的扩展名 */
    private static final String CRASH_REPORTER_EXTENSION = ".cr";

    /**
     * Description: 获取CrashHandler实例 ,单例模式
     * 
     * @author chenzhenlin
     * @date 2012-11-16 下午5:16:09
     * @return CrashHandler实例
     */
    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    /**
     * Description:初始�?,注册Context对象, 获取系统默认的UncaughtException处理�?,
     * 设置该CrashHandler为程序的默认处理�?
     * 
     * @author chenzhenlin
     * @date 2012-11-16 下午5:16:50
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mSettings = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!this.handleException(ex) && (this.mDefaultHandler != null)) {
            // 如果用户没有处理则让系统默认的异常处理器来处�?
            this.mDefaultHandler.uncaughtException(thread, ex);
        }
        // Sleep�?会后结束程序
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error : ", e);
        }
        System.exit(10);
    }

    /**
     * Description: 自定义错误处�?,收集错误信息 发�?�错误报告等操作均在此完�?. �?发�?�可以根据自己的情况来自定义异常处理逻辑
     * 
     * @author chenzhenlin
     * @date 2012-11-17 下午2:28:28
     * @param ex
     * @return 如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        // 收集设备信息
        this.collectCrashDeviceInfo(this.mContext);
        // 保存错误报告文件
        this.saveCrashInfoToFile(ex);
        
        // 使用Toast来显示异常信�?
        Thread t = new Thread() {
            @Override
            public void run() {
                // Looper.prepare();

                // Toast.makeText(CrashHandler.this.mContext, "系统异常,请联系管理员!",
                // Toast.LENGTH_SHORT).show();
                // Looper.loop();
                // 保存错误�?出日�?
//                DatabaseLog log = new DatabaseLog();
//                log.setTableName("S_LOGIN");
//                log.setAuthor(mSettings.getString(Config.USER, ""));
//                log.setAddress(mSettings.getString(Config.USER, ""));
//                log.setTime(SmsUtil.getSystemTime());
//                log.setType(3);
//                DatabaseLogHelper.getDatabaseLogHelper().insert(log);
            }
        };
        t.start();
        // 发�?�错误报告到服务�?
        this.sendCrashReportsToServer(this.mContext);
        return true;
    }

    /**
     * 收集程序崩溃的设备信�?
     * 
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                this.mDeviceCrashInfo.put(CrashHandler.VERSION_NAME,
                        pi.versionName == null ? "not set" : pi.versionName);
                this.mDeviceCrashInfo.put(CrashHandler.VERSION_CODE,
                        pi.versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(CrashHandler.TAG, "Error while collect package info", e);
        }
        // 使用反射来收集设备信�?.在Build类中包含各种设备信息,
        // 例如: 系统版本�?,设备生产�? 等帮助调试程序的有用信息
        // 具体信息请参考后面的截图
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                this.mDeviceCrashInfo.put(field.getName(), field.get(null));
//                if (CrashHandler.DEBUG) {
                    Log.d(CrashHandler.TAG,
                            field.getName() + " : " + field.get(null));
//                }
            } catch (Exception e) {
                Log.e(CrashHandler.TAG, "Error while collect crash info", e);
            }

        }

    }

    /**
     * @param ex
     * @return 保存错误信息到文件中
     */
    private String saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        // 调试状�?�打印日�?
//        if (DEBUG) {
            Log.e(TAG, result);
//        }
        printWriter.close();
        this.mDeviceCrashInfo.put(CrashHandler.STACK_TRACE, result);

        try {
            SimpleDateFormat DateFormatter = new SimpleDateFormat(
                    "yyyyMMddHHmmss");
            String timestamp = DateFormatter.format(new Date(System
                    .currentTimeMillis()));

//            File folder = new File(LOG_FILE_DIR);
//            if (!folder.exists()) {
//                folder.mkdirs();
//            }
//            String fileName = folder + "/crash-" + timestamp
//                    + CrashHandler.CRASH_REPORTER_EXTENSION;
//            FileOutputStream fos = new FileOutputStream(fileName);
//            OutputStreamWriter osw = new OutputStreamWriter(fos);
//            osw.write(result);
//            osw.flush();
//            osw.close();
//            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
        }
        return null;
    }

    /**
     * 在程序启动时�?, 可以调用该函数来发�?�以前没有发送的报告
     */
    public void sendPreviousReportsToServer() {
        this.sendCrashReportsToServer(this.mContext);
    }

    /**
     * 把错误报告发送给服务�?,包含新产生的和以前没发�?�的.
     * 
     * @param ctx
     */
    private void sendCrashReportsToServer(Context ctx) {
//        String[] crFiles = this.getCrashReportFiles(ctx);
//        if ((crFiles != null) && (crFiles.length > 0)) {
            TreeSet<String> sortedFiles = new TreeSet<String>();
//            sortedFiles.addAll(Arrays.asList(crFiles));

            for (String fileName : sortedFiles) {
//                File cr = new File(LOG_FILE_DIR, fileName);
//                this.postReport(cr);
                // cr.delete(); // 删除已发送的报告
            }
//        }
    }

    private void postReport(File cr) {
        postReport();
    }

    /**
     * Description: 使用HTTP Post 发�?�错误报告到服务�?
     * 
     * @author chenzhenlin
     * @date 2012-11-17 下午2:33:15
     * @param file
     */
    public void postReport() {
        // 获取网络状�?�，仅在wifi可用情况下上传日�?
        try {
            ConnectivityManager connMgr = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            if (activeInfo != null && activeInfo.isConnected()
                    && activeInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
//                        postReportToServer();
                    }
                }).start();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 发�?�日志文件到服务�?
     * 
     * @author chenzhenlin
     * @date 2012-11-17 下午2:52:16
     */
//    protected void postReportToServer() {
////        String logurl = mSettings.getString(Config.LOG_SERVICE_URL, "");
////        String user = mSettings.getString(Config.USER, "");
//        if (TextUtils.isEmpty(logurl) || TextUtils.isEmpty(user)) {
//            return;
//        }
////        File[] files = getCrashReportFiles();
////        if (null == files) {
//            return;
//        }
//        DataServiceClient dataClient = new DataServiceClient(logurl);
//        ServiceResult result = dataClient.uploadLogData(user, files);
//        if (result.success) {// 删除日志文件
//            for (File file : files) {
//                file.delete();
//            }
//        }
//    }

//    private File[] getCrashReportFiles() {
//        File filesDir = new File(LOG_FILE_DIR);// ctx.getFilesDir();
//        FilenameFilter filter = new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                return name.endsWith(CrashHandler.CRASH_REPORTER_EXTENSION);
//            }
//        };
//        return filesDir.listFiles(filter);
//    }

    /**
     * * @param ctx
     * 
     * @return 获取错误报告文件名列�?
     */
//    private String[] getCrashReportFiles(Context ctx) {
//        File filesDir = new File(LOG_FILE_DIR);// ctx.getFilesDir();
//        FilenameFilter filter = new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                return name.endsWith(CrashHandler.CRASH_REPORTER_EXTENSION);
//            }
//        };
//        return filesDir.list(filter);
//    }

}
