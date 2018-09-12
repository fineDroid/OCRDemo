package com.baidu.ocr.demo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


import java.io.File;
import java.util.UUID;


public class FileManager {
	private static FileManager instance = new FileManager();
	//缓存目录
	private File mCacheDir;
	//下载缓存目录
	private File mDownloadDir;
	//图片缓存目录
	private File mCacheImageDir;
	//错误日志缓存目录
	private File mErrorDir;

	public static FileManager getInstance() {
		return instance;
	}

	private FileManager() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			mCacheDir = new File(Environment.getExternalStorageDirectory(), "demoju");
		}

		if (!mCacheDir.exists()) {
			mCacheDir.mkdirs();
		}
		mDownloadDir = new File(mCacheDir, "download");
		if (!mDownloadDir.exists()) {
			mDownloadDir.mkdirs();
		}
		mCacheImageDir = new File(mCacheDir, "image");
		if (!mCacheImageDir.exists()) {
			mCacheImageDir.mkdirs();
		}
		mErrorDir = new File(mCacheDir, "error");
		if (!mErrorDir.exists()) {
			mErrorDir.mkdirs();
		}
	}

	private void checkDirExists(File dir) {
		if (!dir.exists()) {
			dir.exists();
		}
	}

	public File getCacheDir() {
		checkDirExists(mCacheDir);
		return mCacheDir;
	}

	public File getCacheImageDir() {
		checkDirExists(mCacheImageDir);
		return mCacheImageDir;
	}

	public File getDownloadDir() {
		checkDirExists(mDownloadDir);
		return mDownloadDir;
	}

	public File getErrorDir() {
		checkDirExists(mErrorDir);
		return mErrorDir;
	}

	/**
	 * 获取文件的原始路径
	 *
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getFileRealPath(Context context, Uri uri) {
		if (null == uri) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * 随机生成文件名
	 *
	 * @return
	 */
	public static String newFileName() {
		return UUID.randomUUID().toString();
	}
}
