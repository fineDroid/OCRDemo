package com.baidu.ocr.demo.biz;

import android.content.Context;

/**
 * @author zhangchengju
 * 主要功能:
 * 创建日期 2018/9/11
 * 作者:longxian
 */
public interface ISignalProcess {

	void init(Context context);

	void close(Context context);

	void handleSignal(Context context, String result);

	void onNextPhotoTask(Context context);

	void resetData(Context context);
}
