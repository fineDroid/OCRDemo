package com.baidu.ocr.demo.data;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.baidu.ocr.demo.util.PreferencesUtils;

import java.util.List;

/**
 * 主要功能:
 * 创建日期 2018/9/11
 */
public class SignalDataManager {

    //第2关是否通过标示
    private static final String KEY_FIRST_CHECK_POINT = "KEY_FIRST_CHECK_POINT";

    //第2关是否通过标示
    private static final String KEY_SECOND_CHECK_POINT = "KEY_SECOND_CHECK_POINT";


    //每次拍照的时间间隔
    private static final String KEY_SIGN_TIME_INETRVAL = "KEY_SIGN_TIME_INETRVAL";

    //扫描不到关键词，重拾5次的标示
    private static final String KEY_SCAN_EMPTY_RETRY_TIMES = "KEY_SCAN_EMPTY_RETRY_TIMES";

    public static void saveFirstCheckPoint(Context context, boolean isOk) {
        PreferencesUtils.putBoolean(context, KEY_FIRST_CHECK_POINT, isOk);
    }

    public static boolean getFirstCheckPoint(Context context) {
        return PreferencesUtils.getBoolean(context, KEY_FIRST_CHECK_POINT, false);
    }

    public static void saveSecondCheckPoint(Context context, boolean isOk) {
        PreferencesUtils.putBoolean(context, KEY_SECOND_CHECK_POINT, isOk);
    }

    public static boolean getSecondCheckPoint(Context context) {
        return PreferencesUtils.getBoolean(context, KEY_SECOND_CHECK_POINT, false);
    }


    public static void savaTimeInterval(Context context, String content) {

        try {
            int interval = Integer.valueOf(content);
            PreferencesUtils.putInt(context, KEY_SIGN_TIME_INETRVAL, interval);
        } catch (Exception e) {
        }
    }

    public static int getTimeInterval(Context context) {
        return PreferencesUtils.getInt(context, KEY_SIGN_TIME_INETRVAL, 30);
    }

    public static void addScanEmptyRetryTimes(Context context) {
        PreferencesUtils.putInt(context, KEY_SCAN_EMPTY_RETRY_TIMES, getScanEmptyRetryTimes(context) + 1);
    }

    public static void resetScanEmptyRetryTimes(Context context) {
        PreferencesUtils.putInt(context, KEY_SCAN_EMPTY_RETRY_TIMES, 0);
    }

    public static int getScanEmptyRetryTimes(Context context) {
        return PreferencesUtils.getInt(context, KEY_SCAN_EMPTY_RETRY_TIMES, 0);
    }
}
