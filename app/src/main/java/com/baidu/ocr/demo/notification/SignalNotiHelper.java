package com.baidu.ocr.demo.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.baidu.ocr.demo.MainActivity;
import com.baidu.ocr.demo.R;
import com.baidu.ocr.demo.util.FileManager;
import com.baidu.ocr.demo.util.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangchengju
 * 主要功能:
 * 创建日期 2018/9/12
 * 作者:longxian
 */
public class SignalNotiHelper {

	public static final String ACTION_WARNING = "ACTION_WARNING_LAOER";

	public static void notify(Context context, String content) {
		try {
			NotificationManagerCompat compat = NotificationManagerCompat.from(context);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

			Intent intent = new Intent(context, MainActivity.class);
			intent.setAction(ACTION_WARNING);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent,
					PendingIntent.FLAG_CANCEL_CURRENT);

			builder.setContentTitle("信号识别警报")
					.setContentText("错误信号：" + content)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setAutoCancel(true)
					.setContentIntent(pendingIntent);

			Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.fans);
			if (sound != null) {
				builder.setSound(sound);
			} else {
				builder.setDefaults(NotificationCompat.DEFAULT_ALL);
			}

			compat.notify(10001, builder.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 获取声音Uri
	 *
	 * @return
	 */
	private static Uri makeSoundUri(Context context, int rawId, String fileName) {
		InputStream is = context.getResources().openRawResource(rawId);
		String filePath = FileManager.getInstance().getCacheDir() + "/sound/" + fileName;
		File file = new File(filePath);
		if (file.exists() && file.length() > 0) {
			return Uri.fromFile(new File(filePath));
		}
		if (FileUtils.writeFileFromIS(file, is, false)) {
			return Uri.fromFile(file);
		}

		return null;
	}
}
