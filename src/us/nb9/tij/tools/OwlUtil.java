package us.nb9.tij.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.mindview.util.TwoTuple;
import us.nb9.tij.TIJAndroidConfig;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Debug;
import android.util.Log;
import android.util.SparseArray;

public class OwlUtil {
	private static final String TAG = "Owl_Util";
	private static final boolean DEBUG = TIJAndroidConfig.DEBUG && true;

	/**
	 * 字符串转换成十六进制字符串
	 */
	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append("0x");
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
			sb.append(", ");
		}
		return sb.toString();
	}

	public static String str2HexStrExt(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	/**
	 * 十六进制转换字符串
	 */
	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * bytes转换成十六进制字符串
	 */
	public static String byte2HexStr(byte[] b) {
		String hs = "0x";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
			hs = hs + ", 0x";
		}
		return hs.toUpperCase();
	}

	/**
	 * 十六进制字符串转换成bytes
	 */
	public static byte[] hexStr2Bytes(String src) {
		int m = 0, n = 0;
		int l = src.length() / 2;
		System.out.println(l);
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
		}
		return ret;
	}

	private static byte uniteBytes(String src0, String src1) {
		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);
		return ret;
	}

	/**
	 * String的字符串转换成unicode的String
	 */
	public static String stringToUnicode(String strText) {
		char c;
		String strRet = "";
		int intAsc;
		String strHex;
		for (int i = 0; i < strText.length(); i++) {
			c = strText.charAt(i);
			intAsc = (int) c;
			strHex = Integer.toHexString(intAsc);
			if (intAsc > 128) {
				strRet += "\\u" + strHex;
			} else {
				// 低位在前面补00
				strRet += "\\u00" + strHex;
			}
		}
		return strRet;
	}

	/**
	 * unicode的String转换成String的字符串
	 */
	public static String unicodeToString(String hex) {
		int t = hex.length() / 6;
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < t; i++) {
			String s = hex.substring(i * 6, (i + 1) * 6);
			// 高位需要补上00再转
			String s1 = s.substring(2, 4) + "00";
			// 低位直接转
			String s2 = s.substring(4);
			// 将16进制的string转为int
			int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
			// 将int转换为字符
			char[] chars = Character.toChars(n);
			str.append(new String(chars));
		}
		return str.toString();
	}

	public static void hexDecode(String str, byte[] ba, int len) {
		char[] cp = str.toCharArray();
		byte nbl = 0;
		int i = 0;
		int icp = 0;
		int iba = 0;

		for (; i < len; i++, icp++) {
			if (cp[icp] >= '0' && cp[icp] <= '9')
				nbl = (byte) (cp[icp] - '0');
			else if (cp[icp] >= 'A' && cp[icp] <= 'F')
				nbl = (byte) (cp[icp] - 'A' + 10);
			else if (cp[icp] >= 'a' && cp[icp] <= 'f')
				nbl = (byte) (cp[icp] - 'a' + 10);

			if ((i & 0x1) == 0)
				ba[iba] = (byte) (nbl << 4);
			else
				ba[iba++] |= nbl;
		}
	}

	public static String bytesToHexString(byte[] bytes) {
		if (bytes == null)
			return null;

		StringBuilder ret = new StringBuilder(2 * bytes.length);

		for (int i = 0; i < bytes.length; i++) {
			int b;

			b = 0x0f & (bytes[i] >> 4);

			ret.append("0123456789abcdef".charAt(b));

			b = 0x0f & bytes[i];

			ret.append("0123456789abcdef".charAt(b));
		}

		return ret.toString();
	}

	public static String bytes2HexString(byte[] bytes) {
		if (bytes == null)
			return null;

		StringBuilder ret = new StringBuilder(2 * bytes.length);

		for (int i = 0; i < bytes.length; i++) {
			int b;

			b = 0x0f & (bytes[i] >> 4);

			ret.append("0123456789ABCDEF".charAt(b));

			b = 0x0f & bytes[i];

			ret.append("0123456789ABCDEF".charAt(b));
		}

		return ret.toString();
	}

	public static String getTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStamp = sdf.format(new Date(System.currentTimeMillis()));
		return timeStamp;
	}

	// public static String getVersionName(Context context, Class cls)
	// {
	// try {
	// ComponentName comp = new ComponentName(context, cls);
	// PackageInfo pinfo =
	// context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
	// return pinfo.versionName;
	// } catch (android.content.pm.PackageManager.NameNotFoundException e) {
	// return null;
	// }
	// }

	/*
	 * 获取当前程序的版本号
	 */
	public static String getVersionName(Context context) throws NameNotFoundException {
		logd(TAG, "Enter getVersionName()");
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		String sPackageName = context.getPackageName();
		if (DEBUG)
			OwlUtil.logi(TAG, "sPackageName = " + sPackageName);
		PackageInfo packInfo = packageManager.getPackageInfo(sPackageName, 0);
		if (DEBUG)
			OwlUtil.logi(TAG, "packInfo.versionName = " + packInfo.versionName);
		return packInfo.versionName;
	}

	public static byte[] getVersionNameByte(Context context) {
		logd(TAG, "Enter getVersionNameByte()");
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		String sPackageName = context.getPackageName();
		if (DEBUG)
			OwlUtil.logi(TAG, "sPackageName = " + sPackageName);
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(sPackageName, 0);
		} catch (NameNotFoundException e) {
			loge(TAG, "NameNotFoundException...");
			e.printStackTrace();
		}
		if (DEBUG)
			OwlUtil.logi(TAG, "packInfo.versionName = " + packInfo.versionName);
		String versionName = packInfo.versionName; // packInfo.versionName =
													// 0.0.11.1
		byte[] byteVer = new byte[4];
		int start = 0;
		int end = 0;
		end = versionName.indexOf('.', start);
		String ver = versionName.substring(start, end);
		byteVer[0] = Byte.valueOf(ver);

		start = end + 1;
		end = versionName.indexOf('.', start);
		ver = versionName.substring(start, end);
		byteVer[1] = Byte.valueOf(ver);

		start = end + 1;
		end = versionName.indexOf('.', start);
		ver = versionName.substring(start, end);
		byteVer[2] = Byte.valueOf(ver);

		start = end + 1;
		end = versionName.length();
		ver = versionName.substring(start, end);
		byteVer[3] = Byte.valueOf(ver);
		logd(TAG, "btyeVer = " + OwlUtil.byte2HexStr(byteVer));

		// String[] sVerDetailStrings = Pattern.compile(".").split(versionName);
		// String[] sVerDetailStrings = packInfo.versionName.split(".");
		// if (DEBUG) Log.v(TAG, "sVerDetailStrings = " + sVerDetailStrings[0]);
		// int i = 0;
		// for (String ver : sVerDetailStrings) {
		// byteVer[i++] = Byte.valueOf(ver);
		// if (DEBUG) OwlUtil.logi(TAG, "byteVer[" + (i-1) + "] = " +
		// byteVer[i-1]);
		// }
		// for (i=0; i<4; i++) {
		// byteVer[i] = Byte.valueOf(sVerDetailStrings[i]);
		// }
		return byteVer;
	}

	public String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	// 2011-11-01 23:59:59
	// 0123456789012345678
	public static long getMilliseconds(String stringTime) {
		String year = stringTime.substring(0, 4);
		String month = stringTime.substring(5, 7);
		String day = stringTime.substring(8, 10);
		String hour = stringTime.substring(11, 13);
		String minute = stringTime.substring(14, 16);
		String second = stringTime.substring(17, 19);

		int iYear = Integer.valueOf(year);
		int iMonth = Integer.valueOf(month);
		int iDay = Integer.valueOf(day);
		int iHour = Integer.valueOf(hour);
		int iMinute = Integer.valueOf(minute);
		int iSecond = Integer.valueOf(second);
		if (DEBUG)
			OwlUtil.logi(TAG, "iYear = " + iYear + ", iMonth = " + iMonth
					+ ", iDay = " + iDay);
		if (DEBUG)
			OwlUtil.logi(TAG, "iHour = " + iHour + ", iMinute = " + iMinute
					+ ", iSecond = " + iSecond);

		Calendar c = Calendar.getInstance();
		// c.setTimeInMillis(System.currentTimeMillis());
		c.set(iYear, iMonth - 1, iDay, iHour, iMinute, iSecond);
		long ms = c.getTimeInMillis();
		logd(TAG, "ms = " + ms);
		if (DEBUG) {
			c.setTimeInMillis(ms);
			logd(TAG, "c.getTime() = " + c.getTime().toLocaleString());
		}
		return ms;
	}

	// 00:01:00
	// 01234567
	public static long getMilliseconds1(String stringTime) {
		String hour = stringTime.substring(0, 2);
		String minute = stringTime.substring(3, 5);
		String second = stringTime.substring(6, 8);

		int iYear = 1970;
		int iMonth = 1;
		int iDay = 1;
		int iHour = Integer.valueOf(hour);
		int iMinute = Integer.valueOf(minute);
		int iSecond = Integer.valueOf(second);
		if (DEBUG)
			OwlUtil.logi(TAG, "iYear = " + iYear + ", iMonth = " + iMonth
					+ ", iDay = " + iDay);
		if (DEBUG)
			OwlUtil.logi(TAG, "iHour = " + iHour + ", iMinute = " + iMinute
					+ ", iSecond = " + iSecond);

		Calendar c = Calendar.getInstance();
		// c.setTimeInMillis(System.currentTimeMillis());
		c.set(iYear, iMonth - 1, iDay, iHour + 8, iMinute, iSecond);
		long ms = c.getTimeInMillis();
		logd(TAG, "ms = " + ms);
		if (DEBUG) {
			c.setTimeInMillis(ms);
			logd(TAG, "c.getTime() = " + c.getTime().toLocaleString());
		}
		return ms;
	}

	/**
	 * 
	 * @param serString
	 *            The service name which looks like
	 *            'us.nb9.androidinfo.service.AndroidSystemInfo'
	 * @return If service is running , return true
	 */
	public static boolean isServiceWorked(Context context, String serString) {
		ActivityManager myManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningService) {
			if (runningServiceInfo.service.getClassName().toString()
					.equals(serString)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return 1: From Owl; 2: From Badger.
	 */
	public static int getComeFrom(Context context) {
		if (DEBUG)
			OwlUtil.logv(TAG, "Enter getComeFrom()");
		Uri CONTENT_URI = Uri.parse("content://com.eversino.owl/Mercury");
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(CONTENT_URI, null, null, null, null);
		if (DEBUG)
			logd(TAG, "cursor = " + cursor);
		int ret = -1;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				int iFrom = cursor.getInt(cursor.getColumnIndex("fromowl"));
				if (DEBUG)
					logi(TAG, "iFrom = " + iFrom);
				ret = iFrom;
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param iFrom
	 *            1: From Owl; 2: From Badger.
	 */
	public static void setComeFrom(Context context, int iFrom) {
		if (DEBUG)
			OwlUtil.logv(TAG, "Enter setComeFrom(), iFrom = " + iFrom);
		Uri CONTENT_URI = Uri.parse("content://com.eversino.owl/Mercury");
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("fromowl", iFrom);
		int ret = cr.update(CONTENT_URI, values, null, null);
		if (DEBUG)
			OwlUtil.logd(TAG, "cr.update return " + ret);
	}

	/**
	 * 
	 * @return 1: Yes, it is gone; 2: Not yet.
	 */
	public static int getVenus(Context context) {
		if (DEBUG)
			OwlUtil.logv(TAG, "Enter getVenus()");
		Uri CONTENT_URI = Uri.parse("content://com.eversino.owl/Venus");
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(CONTENT_URI, null, null, null, null);
		if (DEBUG)
			logd(TAG, "cursor = " + cursor);
		int ret = 2;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				int iGone = cursor.getInt(cursor.getColumnIndex("hasgone"));
				if (DEBUG)
					logi(TAG, "iGone = " + iGone);
				ret = iGone;
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param iGone
	 *            1: Yes, it is gone; 2: Not yet.
	 */
	public static void setVenus(Context context, int iGone) {
		if (DEBUG)
			OwlUtil.logv(TAG, "Enter setVenus(), iGone = " + iGone);
		Uri CONTENT_URI = Uri.parse("content://com.eversino.owl/Venus");
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("hasgone", iGone);
		int ret = cr.update(CONTENT_URI, values, null, null);
		if (DEBUG)
			OwlUtil.logd(TAG, "cr.update return " + ret);
	}
	
	public static void setEarth(Context context, int testWhat) {
		if (DEBUG) OwlUtil.logv(TAG, "Enter setEarth(), testWhat = " + testWhat);
		Uri CONTENT_URI = Uri.parse("content://com.eversino.owl/Earth");
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("testwhat", testWhat);
		int ret = cr.update(CONTENT_URI, values, null, null);
		if (DEBUG)
			OwlUtil.logd(TAG, "cr.update return " + ret);
	}

	public static int getEarth(Context context) {
		if (DEBUG) OwlUtil.logv(TAG, "Enter getEarth()");
		Uri CONTENT_URI = Uri.parse("content://com.eversino.owl/Earth");
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(CONTENT_URI, null, null, null, null);
		if (DEBUG) logd(TAG, "cursor = " + cursor);
		int ret = 2;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				int tw = cursor.getInt(cursor.getColumnIndex("testwhat"));
				if (DEBUG) logi(TAG, "testwhat = " + tw);
				ret = tw;
			}
		}
		return ret;
	}
	
	public static void setMars(Context context, ContentValues cv) {
		if (DEBUG) OwlUtil.logv(TAG, "Enter setMars(), cv = " + cv);
		Uri CONTENT_URI = Uri.parse("content://com.eversino.owl/Mars");
		ContentResolver cr = context.getContentResolver();
		int ret = cr.update(CONTENT_URI, cv, null, null);
		if (DEBUG) OwlUtil.logd(TAG, "cr.update return " + ret);
	}
	
	public static TwoTuple<Integer, Integer> getMars(Context context) {
		if (DEBUG) OwlUtil.logv(TAG, "Enter getMars()");
		Uri CONTENT_URI = Uri.parse("content://com.eversino.owl/Mars");
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(CONTENT_URI, null, null, null, null);
		if (DEBUG) logd(TAG, "cursor = " + cursor);
		int iDoTest = 0;
		int iTestType = 0;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				iDoTest = cursor.getInt(cursor.getColumnIndex("gbDoTest"));
				iTestType = cursor.getInt(cursor.getColumnIndex("gTestType"));
				if (DEBUG) logi(TAG, "iDoTest = " + iDoTest + ", iTestType = " + iTestType);
			}
		}
		return new TwoTuple<Integer, Integer>(iDoTest, iTestType);
	}

	/**
	 * 
	 * @param context
	 * @return 1:正在测试, Others:没有在测试
	 */
	public static int getMarsIfTestting(Context context) {
		if (DEBUG) OwlUtil.logv(TAG, "Enter getMarsIfTestting()");
		Uri CONTENT_URI = Uri.parse("content://com.eversino.owl/Mars");
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(CONTENT_URI, null, null, null, null);
		if (DEBUG) logd(TAG, "cursor = " + cursor);
		int ret = -1;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				int tw = cursor.getInt(cursor.getColumnIndex("gbDoTest"));
				if (DEBUG) logi(TAG, "ifTestting = " + tw);
				ret = tw;
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * @param context
	 * @return 0:手动    1:自动测试
	 */
	public static int getMarsTestType(Context context) {
		if (DEBUG) OwlUtil.logv(TAG, "Enter getMarsTestType(), context = " + context);
		Uri CONTENT_URI = Uri.parse("content://com.eversino.owl/Mars");
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(CONTENT_URI, null, null, null, null);
		if (DEBUG) logd(TAG, "cursor = " + cursor);
		int ret = -1;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				int tw = cursor.getInt(cursor.getColumnIndex("gTestType"));
				if (DEBUG) logi(TAG, "testType = " + tw);
				ret = tw;
			}
		}
		return ret;
	}
	
	public static boolean isAppOnForeground(Context context, String pkgName) {
		OwlUtil.logv(TAG, "Enter isAppOnForeground(), pkgName = " + pkgName);
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		// Returns a list of application processes that are running on the
		// device
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		// String packageName = context.getPackageName();
		String packageName = pkgName;
		OwlUtil.logd(TAG, "appProcesses.size() = " + appProcesses.size());
		boolean ret = false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			printProcessInfo(appProcess);
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				ret = true;
			}
		}
		OwlUtil.logv(TAG, "Leave isAppOnForeground(), ret = " + ret);
		return ret;
	}

	private static SparseArray<String> mapImportanceType;
	static {
		mapImportanceType = new SparseArray<String>();
		mapImportanceType.put(RunningAppProcessInfo.IMPORTANCE_BACKGROUND,
				"IMPORTANCE_BACKGROUND");
		mapImportanceType.put(RunningAppProcessInfo.IMPORTANCE_EMPTY,
				"IMPORTANCE_EMPTY");
		mapImportanceType.put(RunningAppProcessInfo.IMPORTANCE_FOREGROUND,
				"IMPORTANCE_FOREGROUND");
		mapImportanceType.put(RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE,
				"IMPORTANCE_PERCEPTIBLE");
		mapImportanceType.put(RunningAppProcessInfo.IMPORTANCE_SERVICE,
				"IMPORTANCE_SERVICE");
		mapImportanceType.put(RunningAppProcessInfo.IMPORTANCE_VISIBLE,
				"IMPORTANCE_VISIBLE");

		mapImportanceType.put(RunningAppProcessInfo.REASON_PROVIDER_IN_USE,
				"REASON_PROVIDER_IN_USE");
		mapImportanceType.put(RunningAppProcessInfo.REASON_SERVICE_IN_USE,
				"REASON_SERVICE_IN_USE");
		mapImportanceType.put(RunningAppProcessInfo.REASON_UNKNOWN,
				"REASON_UNKNOWN");
	}

	public static void printProcessInfoAll(RunningAppProcessInfo processInfo) {
		OwlUtil.logd(TAG,
				"importance = " + mapImportanceType.get(processInfo.importance));
		OwlUtil.logd(
				TAG, "importanceReasonCode = " + mapImportanceType.get(processInfo.importanceReasonCode));
		OwlUtil.logd(TAG, "importanceReasonComponent = "
				+ processInfo.importanceReasonComponent);
		OwlUtil.logd(TAG, "importanceReasonPid = "
				+ processInfo.importanceReasonPid);
		OwlUtil.logd(TAG, "lru = " + processInfo.lru);
		OwlUtil.logd(TAG, "pid = " + processInfo.pid);
		OwlUtil.logd(TAG, "uid = " + processInfo.uid);
		OwlUtil.logd(TAG, "processName = " + processInfo.processName);
		OwlUtil.logd(TAG, "pkgList.length = " + processInfo.pkgList.length);
		for (String pkgName : processInfo.pkgList) {
			OwlUtil.logi(TAG, "pkgName = " + pkgName);
		}
		int iDc = processInfo.describeContents();
		OwlUtil.logd(TAG, "describeContents() = " + String.format("%X", iDc));
	}

	public static void printProcessInfo(RunningAppProcessInfo processInfo) {
		OwlUtil.logd(TAG, "pid = " + processInfo.pid + ", processName = " + processInfo.processName);
	}
	
	public static void printAllRunningProcess(Context context) {
		OwlUtil.logv(TAG, "Enter printAllRunningProcess(), context = " + context);
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		// Returns a list of application processes that are running on the
		// device
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		// String packageName = context.getPackageName();
//		String packageName = pkgName;
		OwlUtil.logd(TAG, "appProcesses.size() = " + appProcesses.size());
		boolean ret = false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			printProcessInfo(appProcess);
		}
		OwlUtil.logv(TAG, "Leave printAllRunningProcess(), ret = " + ret);
	}
	
	public static long getProcUsedMemory(Context context, int iPid) {
		if (DEBUG) OwlUtil.logv(TAG, "Enter getProcUsedMemory(), iPid = " + iPid);
		long iMem = 0;
		// 获得该进程占用的内存
		int[] myMempid = new int[] { iPid };
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
		Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
		// 获取进程占内存用信息 kb单位
		iMem = memoryInfo[0].dalvikPrivateDirty;
		return iMem;
	}
	
	public static void logv(String tag, String log) {
		Log.v(tag, "TIJ: " + log);
	}

	public static void logd(String tag, String log) {
		Log.d(tag, "TIJ: " + log);
	}

	public static void logi(String tag, String log) {
		Log.i(tag, "TIJ: " + log);
	}

	public static void logw(String tag, String log) {
		Log.w(tag, "TIJ: " + log);
	}

	public static void loge(String tag, String log) {
		Log.e(tag, "TIJ: " + log);
	}

	public static void loge(String tag, String msg, Throwable tr) {
		Log.e(tag, msg, tr);
	}
}