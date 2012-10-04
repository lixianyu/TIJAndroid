package us.nb9.tij;

import us.nb9.tij.tools.OwlUtil;
import android.app.Application;
import android.content.res.Configuration;

public class TIJApp extends Application {
    private static final String TAG = "TIJApp";
    private static final boolean DEBUG = TIJAndroidConfig.DEBUG&&true;

    private static TIJApp sAlpacaApp = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) OwlUtil.logi(TAG, "Enter onCreate(), process id = " + android.os.Process.myPid());

        sAlpacaApp = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (DEBUG) OwlUtil.logi(TAG, "Enter onTerminate()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (DEBUG) OwlUtil.logi(TAG, "Enter onConfigurationChanged(), newConfig = " + newConfig);
    }

    public synchronized static TIJApp getApplication() {
        return sAlpacaApp;
    }
}
