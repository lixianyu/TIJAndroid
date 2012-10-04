package us.nb9.tij;

import static net.mindview.util.Print.*;
import us.nb9.tij.dotest.TIJTest;
import us.nb9.tij.dotest.TIJTestFactory;
import android.app.Activity;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

public class TIJAndroidActivity extends Activity {
	private static String TAG = "TIJAndroidActivity";
	private static final boolean DEBUG = TIJAndroidConfig.DEBUG && true;

	private static TIJAndroidActivity mInstance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mInstance = this;
		TIJAndroidConfig.init(mInstance);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (DEBUG)
			print_v(TAG, "Enter onResume()");
		runTest();
		MobclickAgent.onResume(this);
		if (DEBUG)
			print_v(TAG, "Leave onResume()");
	}

	@Override
	public void onPause() {
		super.onPause();
		if (DEBUG)
			print_v(TAG, "Enter onPause()");
		MobclickAgent.onPause(this);
		if (DEBUG)
			print_v(TAG, "Leave onPause()");
	}

	private void runTest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				TIJTest tijt = TIJTestFactory.createTest();
				tijt.doTest();
			}
		}).start();
	}
}
