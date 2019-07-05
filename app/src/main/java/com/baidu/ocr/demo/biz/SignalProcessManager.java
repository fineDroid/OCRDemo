package com.baidu.ocr.demo.biz;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baidu.ocr.demo.FileUtil;
import com.baidu.ocr.demo.MainActivity;
import com.baidu.ocr.demo.data.SignalDataManager;
import com.baidu.ocr.demo.data.SignalDataModel;
import com.baidu.ocr.demo.data.WarningModel;
import com.baidu.ocr.demo.notification.SignalNotiHelper;
import com.baidu.ocr.demo.task.AlarmTaskManager;
import com.baidu.ocr.demo.task.NotifyContentEvent;
import com.baidu.ocr.ui.camera.CameraActivity;

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
        resetData(context);
    }

    @Override
    public void handleSignal(Context context, String resultJson) {
        Log.d("juju", "resultJson: " + resultJson);
        SignalProcessManager.getInstance().onNextPhotoTask(context);

        SignalDataModel signalDataModel = JSON.parseObject(resultJson, SignalDataModel.class);

        StringBuilder scanResultStringBuilder = new StringBuilder();
        if (signalDataModel != null) {
            List<SignalDataModel.WordResult> results = signalDataModel.getWords_result();
            if (results != null && results.size() != 0) {
                for (SignalDataModel.WordResult wordResult : results) {
                    scanResultStringBuilder.append(wordResult.getWords());
                    scanResultStringBuilder.append("\n");
                    scanResultStringBuilder.append("\n");
                }
                if (checkScanResult(scanResultStringBuilder.toString())) {

                    EventBus.getDefault().post(new NotifyContentEvent(scanResultStringBuilder.toString()));
                    handleScanValid(context, scanResultStringBuilder.toString());

                } else {
                    EventBus.getDefault().post(new NotifyContentEvent("啥也没扫描到"));
                    handleScanEmpty(context);
                }
            } else {
                EventBus.getDefault().post(new NotifyContentEvent("啥也没扫描到"));
                handleScanEmpty(context);
            }
        } else {
            EventBus.getDefault().post(new NotifyContentEvent("啥也没扫描到"));
            handleScanEmpty(context);
        }
    }

    private void handleScanValid(Context context, String content) {
        SignalDataManager.resetScanEmptyRetryTimes(context);

        if (content.contains("事故")) {
            closeTask(context);
            notifyError(context, "拍到了事故！！！！");
            //多次提醒
            onNextErrorWarningTask(context);
            return;
        }

        //TODO
        int currentF1 = 0;
        int currentF2 = 0;

        //第一关没过
        if (!SignalDataManager.getFirstCheckPoint(context)) {

        }

    }

    private void handleScanEmpty(Context context) {
        if (SignalDataManager.getScanEmptyRetryTimes(context) >= 5) {
            closeTask(context);
            //只错误提醒一次
            notifyError(context, "拍不到东西，重试超过5次了");
        } else {
            //没拍到东西，5次重试+1
            SignalDataManager.addScanEmptyRetryTimes(context);
            SignalProcessManager.getInstance().onNextPhotoTask(context);
        }
    }

    private boolean checkScanResult(String content) {
        if (content.contains("事故") || content.contains("事故")
                || content.contains("事故") || content.contains("事故")
                || content.contains("事故") || content.contains("事故")) {
            return true;

        }
        return false;
    }

    @Override
    public void onNextPhotoTask(Context context) {
        //时间间隔秒
        int timeInterval = SignalDataManager.getTimeInterval(context);
        AlarmTaskManager.startOnceTask(context, timeInterval * 1000, AlarmTaskManager.TASK_TAKE_PHOTO);
    }

    @Override
    public void onNextErrorWarningTask(Context context) {
        AlarmTaskManager.startOnceTask(context, 3 * 1000, AlarmTaskManager.TASK_ERROR_NOTIFY_THREE_SECOND);
    }

    @Override
    public void resetData(Context context) {
        SignalDataManager.saveFirstCheckPoint(context, false);
        SignalDataManager.saveSecondCheckPoint(context, false);
        SignalDataManager.resetScanEmptyRetryTimes(context);
    }

    @Override
    public void notifyError(Context context, String desc) {
        SignalNotiHelper.notify(context, desc);
    }


    private static class SignalProcessHolder {
        private static final SignalProcessManager INSTANCE = new SignalProcessManager();
    }
}
