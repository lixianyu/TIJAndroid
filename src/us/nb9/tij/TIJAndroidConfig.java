package us.nb9.tij;

import static net.mindview.util.Print.*;
import java.io.File;

import us.nb9.tij.tools.InitResources;
import us.nb9.tij.tools.ToastService;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class TIJAndroidConfig {
	private static final String TAG = "TIJAndroidConfig";
	public static boolean DEBUG = true;
	
	public static String EXTERNAL_DIR; // /mnt/sdcard/TIJAndroid
	public static String INTERNAL_DIR; // /data/data/us.nb9.tij/files/Java
	
	public static void init(Context context) {
		createDir();
		initFiles(context);
		initUmeng(context);
	}
	
	private static void initFiles(Context context) {
		if (DEBUG) print_v(TAG, "Enter initFiles()");
		InitResources irs = new InitResources(context);
		irs.CopyAssets("Java", INTERNAL_DIR);
		if (DEBUG) print_v(TAG, "Enter initFiles()");
	}
	
	private static void createDir() {
		if (DEBUG) print_d(TAG, "Enter createDir()");
		String eFileString = Environment.getExternalStorageDirectory().getAbsolutePath();
		EXTERNAL_DIR = eFileString + File.separator + "TIJAndroid";
		INTERNAL_DIR = TIJApp.getApplication().getFilesDir().getAbsolutePath() +
				File.separator + "Java";
		String curDir = " ";
		boolean mkOk = false;
		for (int i = 0; i < 2; i++) {
			if (i == 0) {
				curDir = EXTERNAL_DIR;
			}
			else if (i == 1) {
				curDir = INTERNAL_DIR;
			}
			
			File file = new File(curDir);
			if (!file.isDirectory()) {
				mkOk = file.mkdirs();
				if (!mkOk) { //Try again.
					mkOk = file.mkdirs();
				}
				else {
					if (DEBUG) print_i(TAG, "created dir : " + i + "..." + curDir);
				}
			}
			else {
				if (DEBUG) print_v(TAG, curDir + " already exist!");
			}
		}
		if (DEBUG) print_d(TAG, "Leave createDir()");
	}
	
	public static void initUmeng(Context context) {
		MobclickAgent.setDebugMode(false);
		MobclickAgent.setAutoLocation(true);
		MobclickAgent.setSessionContinueMillis(2000);
		MobclickAgent.onError(context);
		MobclickAgent.updateOnlineConfig(context);

		initUmengUpdate(context);
	}

	private static void initUmengUpdate(final Context context) {
		// ������������ʱ�Զ�����Ƿ���Ҫ���£� ���������д������Activity ��onCreate()�����
		com.umeng.common.Log.LOG = false;
		UmengUpdateAgent.setUpdateOnlyWifi(false); // Ŀǰ����Ĭ����Wi-Fi��������²Ž����Զ����ѡ�����Ҫ���������绷���½��и����Զ����ѣ�������Ӹ��д���
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateListener updateListener = new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				if (DEBUG)
					print_v(TAG, "Enter onUpdateReturned(), updateStatus = "
							+ updateStatus);
				
				switch (updateStatus) {
				case 0: // has update
					UmengUpdateAgent.showUpdateDialog(context, updateInfo);
					break;
				case 1: // has no update
					ToastService.toast("û�и���", Toast.LENGTH_SHORT);
					break;
				case 2: // none wifi
					ToastService.toast("û��wifi���ӣ� ֻ��wifi�¸���", Toast.LENGTH_SHORT);
					break;
				case 3: // time out
					ToastService.toast("��ʱ", Toast.LENGTH_SHORT);
					break;
				}
			}
		};
		UmengUpdateAgent.setUpdateListener(updateListener);
		UmengUpdateAgent.setOnDownloadListener(new UmengDownloadListener() {
			@Override
			public void OnDownloadEnd(int result) {
				if (DEBUG)
					print_d(TAG, "download result : " + result);
				ToastService.toast("download result : " + result,
						Toast.LENGTH_LONG);
			}
		});
		UmengUpdateAgent.update(context);
	}
}
