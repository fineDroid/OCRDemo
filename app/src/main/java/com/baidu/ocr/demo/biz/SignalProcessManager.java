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

import java.util.ArrayList;
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
		AlarmTaskManager.stopOnceTask(context);
		mContext = context.getApplicationContext();
	}

	@Override
	public void close(Context context) {
		AlarmTaskManager.stopOnceTask(context);
	}

	@Override
	public void handleSignal(Context context, String resultJson) {
		Log.d("juju", resultJson);

		SignalDataModel signalDataModel = JSON.parseObject(resultJson, SignalDataModel.class);

		StringBuilder stringBuilder = new StringBuilder();
		List<SignalDataModel.WordResult> results = signalDataModel.getWords_result();
		if (results != null && results.size() != 0) {
			for (SignalDataModel.WordResult wordResult : results) {
				stringBuilder.append(wordResult.getWords());
				stringBuilder.append("\n");
				stringBuilder.append("\n");
			}
			EventBus.getDefault().post(new NotifyContentEvent(stringBuilder.toString()));
		}

		//检查报警
		boolean isError = checkErrorSignal(signalDataModel);
		if (isError) {
			resetData(mContext);
		} else {
			//先更新--新增表
			updateLastAddedSignals(signalDataModel);

			//再更新--原信号表
			updataSourceSingals(signalDataModel);
		}
	}

	@Override
	public void onNextPhotoTask(Context context) {
		//时间间隔秒
		int timeInterval = SignalDataManager.getTimeInterval(context);
		AlarmTaskManager.startOnceTask(context, timeInterval * 1000);
	}

	@Override
	public void resetData(Context context) {
		SignalDataManager.saveLastAddedSignals(context, 0);
		SignalDataManager.saveSourceSignals(context, 0);
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

		//没有新增信号，不报警
		int lastAddWordResults = SignalDataManager.readLastAddedSignals(mContext);
		if (lastAddWordResults == 0) {
			return warningModel;
		}

		//当前>=lastAddWordResults 报警
		if (currentWords.size() >= lastAddWordResults) {
			warningModel.setNeedWarning(true);
			warningModel.setContent("老二,有错误信号啦");
			return warningModel;
		}
		return warningModel;
	}


	private void updateLastAddedSignals(SignalDataModel signalDataModel) {
		//没有新信号，不存
		if (signalDataModel == null) {
			return;
		}
		//没有新信号，不存
		List<SignalDataModel.WordResult> currentWordResults = signalDataModel.getWords_result();
		if (currentWordResults == null || currentWordResults.size() == 0) {
			return;
		}


		int lastSourceNums = SignalDataManager.readSourceSignals(mContext);
		if (currentWordResults.size() > lastSourceNums) {
			//把新增信号存在SP
			SignalDataManager.saveLastAddedSignals(mContext, currentWordResults.size());
		}
		//没有新增，强制更新为初始状态

	}

	private void updataSourceSingals(SignalDataModel signalDataModel) {
		if (signalDataModel == null) {
			return;
		}
		List<SignalDataModel.WordResult> currentWordResults = signalDataModel.getWords_result();
		if (currentWordResults == null || currentWordResults.size() == 0) {
			return;
		}

		SignalDataManager.saveSourceSignals(mContext, currentWordResults.size());
	}


	private static class SignalProcessHolder {
		private static final SignalProcessManager INSTANCE = new SignalProcessManager();
	}
}
