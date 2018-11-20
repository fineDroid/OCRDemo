package com.baidu.ocr.demo.task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.baidu.ocr.demo.biz.BackgroundService;

/**
 * Created by cjzhang on 16/7/14.
 * 闹钟帮助类
 */
public class AlarmTaskManager {
	private static final String TAG = "TimeSetter";
	public static final int TASK_TEN_MIN = 10001;
	public static final int TASK_TWO_SECOND = 10002;

	public static void startOnceTask(Context context, long intervalMillis, int task) {
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		//API大于等于19后,set方法是不精准的
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
					intervalMillis + System.currentTimeMillis(), getPendingIntent(context, task));
		} else if (Build.VERSION.SDK_INT >= 19) {
			manager.setExact(AlarmManager.RTC_WAKEUP,
					intervalMillis + System.currentTimeMillis(), getPendingIntent(context, task));
		} else {
			manager.set(AlarmManager.RTC_WAKEUP,
					intervalMillis + System.currentTimeMillis(), getPendingIntent(context, task));
		}
	}

	private static PendingIntent getPendingIntent(Context context, int task) {
		Intent intent = new Intent(context, BackgroundService.class);
		intent.setAction(BackgroundService.ACTION_ALARM_TASK_TIME_OVER);
		intent.putExtra(BackgroundService.EXTRA_ALARM_TASK_TIME_OVER_ID, task);
		return PendingIntent.getService(context, task, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	/**
	 * 取消拍照闹钟
	 */
	public static void stopTakePhotoTask(Context context) {
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.cancel(getPendingIntent(context, TASK_TEN_MIN));
	}

	/**
	 * 取消报警闹钟
	 */
	public static void stopWaringTask(Context context) {
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.cancel(getPendingIntent(context, TASK_TWO_SECOND));
	}
}
