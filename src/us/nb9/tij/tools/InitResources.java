package us.nb9.tij.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import us.nb9.tij.TIJAndroidConfig;

import android.content.Context;
import android.content.res.AssetManager;

public class InitResources {
	private static final String TAG = "InitResources";
	private static final boolean DEBUG = TIJAndroidConfig.DEBUG && true;

	private Context _context;

	public InitResources(Context context) {
		_context = context;
	}

	/**
	 * 把assets中的指定目录下的所有文件拷贝到目标目录
	 * @param srcdirname
	 * @param destdirname
	 */
	public void CopyAssets(String srcdirname, String destdirname) {
		AssetManager assetManager = _context.getAssets();
		String[] files = null;
		try {
			files = assetManager.list(srcdirname);
			if (DEBUG) {
				for (String aFile : files) {
					OwlUtil.logi(TAG, "aFile = " + aFile);
				}
			}
		} catch (IOException e) {
			OwlUtil.loge(TAG, e.getMessage());
		}
		InputStream in = null;
		OutputStream out = null;
		for (int i = 0; i < files.length; i++) {
			try {
				in = assetManager.open(srcdirname + File.separator + files[i]);
				out = new FileOutputStream(destdirname
						+ File.separator
						+ files[i]);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (IOException e) {
				OwlUtil.loge(TAG, files[i]);
				OwlUtil.loge(TAG, e.toString());
			}
		}
	}
	
	/**
	 * 从assets指定目中拷贝一个文件到/data/data/com.eversino.owl/files目录下面
	 * @param dirname A relative path within the assets
	 * @param renFrom 源文件名
	 * @param renTo 目标文件名
	 */
	public void CopyAssets(String dirname, String renFrom, String renTo) {
		AssetManager assetManager = _context.getAssets();
		String[] files = null;
		try {
			files = assetManager.list(dirname);
			if (DEBUG) {
				for (String aFile : files) {
					OwlUtil.logv(TAG, "aFile = " + aFile);
				}
			}
		} catch (IOException e) {
			OwlUtil.loge(TAG, e.getMessage());
		}
		InputStream in = null;
		OutputStream out = null;
		for (int i = 0; i < files.length; i++) {
			if (!renFrom.equals(files[i])) {
				continue;
			}
			try {
				in = assetManager.open(dirname + "/" + files[i]);
				out = new FileOutputStream(_context.getFilesDir()
						.getAbsolutePath()
						+ "/"
						+ rename(files[i], renFrom, renTo));
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (IOException e) {
				OwlUtil.loge(TAG, files[i]);
				OwlUtil.loge(TAG, e.toString());
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	private String rename(String filename, String renFrom, String renTo) {
		return filename.replaceAll(renFrom, renTo);
	}
}
