package us.nb9.tij.tools;

import static net.mindview.util.Print.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import us.nb9.tij.TIJAndroidConfig;
import us.nb9.tij.TIJApp;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

public class ToastService extends Service {
	private static final String TAG = "ToastService";
    private static final boolean DEBUG = TIJAndroidConfig.DEBUG && true;

    private static final String format = "yyyy-MM-dd HH:mm:ss";
    
    private static String mLastStr;
    private static long mLastTime;
    private static ToastService mInstance = null;
    private Looper mServiceLooper;
    private static ServiceHandler mServiceHandler;
    private static boolean mStarted;

    private static final int EVENT_QUIT = 5000;
    private static final int EVENT_STOP_SESSION = 5001;
    private static final int EVENT_DO = 5002;
    private static final int EVENT_TOAST = 5003;
    
    // Handler that receives messages from the thread
    private final static class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
            if (DEBUG) print_v(TAG, "Enter ServiceHandler(), looper = " + looper);
        }

        public void handleMessage(Message msg) {
            if (DEBUG) print_v(TAG, "Enter handleMessage(), msg = " + msg);
            switch (msg.what) {
            case EVENT_QUIT :
                getLooper().quit();
                break;

            case EVENT_STOP_SESSION :
            	mInstance.stopSelf();
            	break;
            	
            case EVENT_DO :
            	break;
            	
            case EVENT_TOAST :
            	String sToast = (String) msg.obj;
            	int duration = msg.arg1;
            	Toast.makeText(mInstance, sToast, duration).show();
            	break;
            	
            default :
                print_w(TAG, "msg.what=" + msg.what);
                return;
            }
        }
    }
    
    @Override
    public void onCreate() {
    	if (DEBUG) print_v(TAG, "Enter onCreate()");
    	mInstance = this;
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ToastService",
                		Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        mStarted = true;
        
        mLastStr = "heihei";
        mLastTime = System.currentTimeMillis();
	    if (DEBUG) print_d(TAG, "Leave onCreate()");
    }
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		if (DEBUG) print_v(TAG, "onStartCommand(), flags = " + flags + 
				", startId=" + startId + ", intent=" + intent);
		if (startId == 1) {
			mStarted = true;
		}
		if (intent != null) {
			if (intent.hasExtra("duration")) {
		        Message msg = mServiceHandler.obtainMessage(EVENT_TOAST);
		        msg.obj = intent.getStringExtra("toastString");
		        msg.arg1 = intent.getIntExtra("duration", Toast.LENGTH_SHORT);
		        if (mServiceHandler.getLooper().getThread().isAlive()) { 
		        	mServiceHandler.sendMessage(msg);
		        }
			}
		}
        return START_STICKY;
    }

	@Override
    public void onDestroy() {
        if (DEBUG) print_v(TAG, "Enter onDestroy()");
        mInstance = null;
        mStarted = false;
        mServiceHandler.sendEmptyMessage(EVENT_QUIT);
    }
	
	private String getTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String timeStamp = sdf.format(new Date(System.currentTimeMillis()));
		if (DEBUG) print_i(TAG, "timeStamp = " + timeStamp);
		//return "2011-08-04 21:20:39";
		return timeStamp;
	}

    private static void start(Context context) {
    	if (DEBUG) print_v(TAG, "Enter start(), context = " + context);
		Intent intent = new Intent(context, ToastService.class);
		context.startService(intent);
		if (DEBUG) print_v(TAG, "Leave start()");
	}

    private static void stop() {
    	if (DEBUG) print_v(TAG, "Enter stop()");
    	if (!OwlUtil.isServiceWorked(TIJApp.getApplication(), "us.nb9.tij.tools.ToastService")) {
    		if (DEBUG) print_d(TAG, "ToastService is not running, just return");
    		return;
    	}
		mServiceHandler.sendEmptyMessage(EVENT_STOP_SESSION);
		if (DEBUG) print_v(TAG, "Leave stop()");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public static void toast(String sToast, int duration) {
		if (System.currentTimeMillis() - mLastTime < 4000) {
			if (sToast.equals(mLastStr)) {
				return;
			}
		}
		
		mLastStr = sToast;
		mLastTime = System.currentTimeMillis();
		if (mStarted) {
			Message msg = mServiceHandler.obtainMessage(EVENT_TOAST);
			msg.obj = sToast;
			msg.arg1 = duration;
			mServiceHandler.sendMessage(msg);
		}
		else {
			Context context = TIJApp.getApplication();
			Intent intent = new Intent(context, ToastService.class);
			intent.putExtra("toastString", sToast);
			intent.putExtra("duration", duration);
			context.startService(intent);
		}
	}
}