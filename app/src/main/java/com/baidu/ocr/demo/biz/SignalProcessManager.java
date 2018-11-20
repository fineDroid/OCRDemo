package com.baidu.ocr.demo.biz;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baidu.ocr.demo.data.SignalDataManager;
import com.baidu.ocr.demo.data.SignalDataModel;
import com.baidu.ocr.demo.data.WarningModel;
import com.baidu.ocr.demo.notification.SignalNotiHelper;
import com.baidu.ocr.demo.task.AlarmTaskManager;
import com.baidu.ocr.demo.task.NotifyContentEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author zhangchengju
 * 主要功能:
 * 创建日期 2018/9/11
 * 作者:longxian
 */
public class SignalProcessManager implements ISignalProcess {
	private static final String TAG = SignalProcessManager.class.getSimpleName();
	private Context mContext;

	public static SignalProcessManager getInstance() {
		return SignalProcessHolder.INSTANCE;
	}

	@Override
	public void init(Context context) {
		context.startService(new Intent(context, BackgroundService.class));
		closeTask(context);
		mContext = context.getApplicationContext();
	}

	@Override
	public void closeTask(Context context) {
		AlarmTaskManager.stopTakePhotoTask(context);
		AlarmTaskManager.stopWaringTask(context);
	}

	@Override
	public void handleSignal(Context context, String resultJson) {
		Log.d("juju", resultJson);

		SignalDataModel signalDataModel = JSON.parseObject(resultJson, SignalDataModel.class);

		StringBuilder stringBuilder = new StringBuilder();

		if (signalDataModel != null) {
			List<SignalDataModel.WordResult> results = signalDataModel.getWords_result();
			if (results != null && results.size() != 0) {
				for (SignalDataModel.WordResult wordResult : results) {
					stringBuilder.append(wordResult.getWords());
					stringBuilder.append("\n");
					stringBuilder.append("\n");
				}
				EventBus.getDefault().post(new NotifyContentEvent(stringBuilder.toString()));
			} else {
				EventBus.getDefault().post(new NotifyContentEvent("啥也没扫描到"));
			}
		} else {
			EventBus.getDefault().post(new NotifyContentEvent("啥也没扫描到"));
		}

		//检查报警
		boolean isError = checkErrorSignal(signalDataModel);
		if (isError) {
			closeTask(mContext);
			resetData(mContext);
			onNextWarningTask(mContext);
		} else {
			//更新--原信号表
			updataSourceSingals(signalDataModel);
		}
	}

	@Override
	public void onNextPhotoTask(Context context) {
		//时间间隔秒
		int timeInterval = SignalDataManager.getTimeInterval(context);
		AlarmTaskManager.startOnceTask(context, timeInterval * 1000, AlarmTaskManager.TASK_TEN_MIN);
	}

	@Override
	public void onNextWarningTask(Context context) {
		AlarmTaskManager.startOnceTask(context, 3 * 1000, AlarmTaskManager.TASK_TWO_SECOND);
	}

	@Override
	public void resetData(Context context) {
		SignalDataManager.resetSourceSignals(context);
	}

	private boolean checkErrorSignal(SignalDataModel signalDataModel) {
		WarningModel warningModel = checkInLastAddedSignalList(signalDataModel);
		if (warningModel.isNeedWarning()) {
			// notify()
			SignalNotiHelper.notify(mContext, warningModel.getContent());
			return true;
		}
		return false;
	}

	private WarningModel checkInLastAddedSignalList(SignalDataModel signalDataModel) {
		WarningModel warningModel = new WarningModel(false, "");
		//没有新信号，不报警
		if (signalDataModel == null) {
			return warningModel;
		}
		//没有新信号，不报警
		List<SignalDataModel.WordResult> currentWords = signalDataModel.getWords_result();
		if (currentWords == null || currentWords.size() == 0) {
			return warningModel;
		}

		//没有新信号，不报警
		String lastLine = currentWords.get(currentWords.size() - 1).getWords();
		if (TextUtils.isEmpty(lastLine)) {
			return warningModel;
		}

		String lastResult = SignalDataManager.readSourceSignals(mContext);
		if (SignalDataManager.DEFAULT_RESULT.equals(lastResult)) {
			return warningModel;
		}

		if (!currentWords.get(currentWords.size() - 1).getWords().equals(lastResult)) {
			warningModel.setNeedWarning(true);
			warningModel.setContent("三哥,有错误信号啦");
			return warningModel;
		}
		return warningModel;
	}


	private void updataSourceSingals(SignalDataModel signalDataModel) {
		if (signalDataModel == null) {
			SignalDataManager.saveSourceSignals(mContext, "");
			return;
		}
		List<SignalDataModel.WordResult> currentWordResults = signalDataModel.getWords_result();
		if (currentWordResults == null || currentWordResults.size() == 0) {
			SignalDataManager.saveSourceSignals(mContext, "");
			return;
		}

		String lastLine = currentWordResults.get(currentWordResults.size() - 1).getWords();
		if (TextUtils.isEmpty(lastLine)) {
			SignalDataManager.saveSourceSignals(mContext, "");
			return;
		}

		SignalDataManager.saveSourceSignals(mContext, lastLine);
	}


	private static class SignalProcessHolder {
		private static final SignalProcessManager INSTANCE = new SignalProcessManager();
	}
}
