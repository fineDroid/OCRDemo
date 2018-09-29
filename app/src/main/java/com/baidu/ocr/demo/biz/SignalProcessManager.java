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
		checkErrorSignal(signalDataModel);

		//先更新--新增表
		updateLastAddedSignals(signalDataModel);

		//再更新--原信号表
		updataSourceSingals(resultJson);
	}

	@Override
	public void onNextPhotoTask(Context context) {
		//时间间隔秒
		int timeInterval = SignalDataManager.getTimeInterval(context);
		AlarmTaskManager.startOnceTask(context, timeInterval * 1000);
	}

	private void checkErrorSignal(SignalDataModel signalDataModel) {
		WarningModel warningModel = checkInLastAddedSignalList(signalDataModel);
		if (warningModel.isNeedWarning()) {
			// notify()
			SignalNotiHelper.notify(mContext, warningModel.getContent());
		}
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
		List<SignalDataModel.WordResult> addWordResults = SignalDataManager.readLastAddedSignals(mContext);
		if (addWordResults == null || addWordResults.size() == 0) {
			return warningModel;
		}

		for (SignalDataModel.WordResult everyCurrentWord : currentWords) {
			Log.d(TAG, "isInLastAddedSignalList()======everyCurrentWord: " + everyCurrentWord.getWords());
			if (TextUtils.isEmpty(everyCurrentWord.getWords())) {
				continue;
			}

			//在新增里，true，报警
			for (SignalDataModel.WordResult everyAdd : addWordResults) {
				Log.d(TAG, "isInLastAddedSignalList()======everyLastAdd: " + everyAdd.getWords());
				if (everyAdd.getWords().equals(everyCurrentWord.getWords())) {
					Log.d(TAG, "isInLastAddedSignalList()======everyLastAdd=everyCurrentWord");
					warningModel.setNeedWarning(true);
					warningModel.setContent(everyCurrentWord.getWords());
					return warningModel;
				}
			}

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

		//没有旧信号，不存
		if (SignalDataManager.readSourceSignals(mContext) == null) {
			return;
		}

		//没有旧信号，不存
		List<SignalDataModel.WordResult> lastWordResults = SignalDataManager.readSourceSignals(mContext).getWords_result();
		if (lastWordResults == null || lastWordResults.size() == 0) {
			return;
		}

		List<SignalDataModel.WordResult> addWordResults = new ArrayList<>();
		//当前信号是新增信号，存
		for (SignalDataModel.WordResult currentWordResult : currentWordResults) {
			if (TextUtils.isEmpty(currentWordResult.getWords())) {
				continue;
			}
			Log.d(TAG, "updateLastAddedSignals()======currentWordResult: " + currentWordResult.getWords());
			if (!inAdd(currentWordResult, lastWordResults)) {
				Log.d(TAG, "updateLastAddedSignals()======currentWordResult: " + currentWordResult + " is not in lastWordResultsList");
				addWordResults.add(currentWordResult);
			}
		}
		//没有新增，强制更新为初始状态
		if (addWordResults.size() == 0) {
			SignalDataModel.WordResult temp = new SignalDataModel.WordResult();
			temp.setWords("默认不为空的数据@@@kobeBryant");
			Log.d(TAG, "updateLastAddedSignals()======addDefault @@@kobeBryant");
			addWordResults.add(temp);
		}

		//把新增信号存在SP
		SignalDataManager.saveLastAddedSignals(mContext, addWordResults);
	}

	private void updataSourceSingals(String resultJson) {
		SignalDataManager.saveSourceSignals(mContext, resultJson);
	}

	private boolean inAdd(SignalDataModel.WordResult currentWordResult, List<SignalDataModel.WordResult> lastWordResults) {
		for (SignalDataModel.WordResult everyLastWord : lastWordResults) {
			if (everyLastWord.getWords().equals(currentWordResult.getWords())) {
				return true;
			}
		}
		return false;
	}

	private static class SignalProcessHolder {
		private static final SignalProcessManager INSTANCE = new SignalProcessManager();
	}
}
