/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ocr.demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.demo.biz.ISignalProcess;
import com.baidu.ocr.demo.biz.SignalProcessManager;
import com.baidu.ocr.demo.data.SignalDataManager;
import com.baidu.ocr.demo.task.NotifyContentEvent;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

	private static final int REQUEST_CODE_GENERAL_BASIC = 106;

	private boolean hasGotToken = false;

	private AlertDialog.Builder alertDialog;

	private ISignalProcess iSignalProcess;
	private TextView mTextView;
	private EditText mEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		alertDialog = new AlertDialog.Builder(this);
		iSignalProcess = SignalProcessManager.getInstance();
		mTextView = (TextView) findViewById(R.id.signal_tv);
		mTextView.clearComposingText();
		mEditText = (EditText) findViewById(R.id.time_interval_et);

		// 通用文字识别--开启信号处理
		findViewById(R.id.general_basic_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkTokenStatus()) {
					return;
				}

				iSignalProcess.init(MainActivity.this);

				Intent intent = new Intent(MainActivity.this, CameraActivity.class);
				intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
						FileUtil.getSaveFile(getApplication()).getAbsolutePath());
				intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
						CameraActivity.CONTENT_TYPE_GENERAL);
				startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
			}
		});

		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.d("", "");
			}

			@Override
			public void afterTextChanged(Editable s) {
				String timeInterval = s.toString();
				SignalDataManager.savaTimeInterval(MainActivity.this, timeInterval);
			}
		});


		mEditText.setText(String.valueOf(SignalDataManager.getTimeInterval(this)));

		//关闭信号处理
		findViewById(R.id.close_task_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iSignalProcess.close(MainActivity.this);
			}
		});

		// 请选择您的初始化方式
		initAccessToken();
		initAccessTokenWithAkSk();
	}

	private boolean checkTokenStatus() {
		if (!hasGotToken) {
			Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
		}
		return hasGotToken;
	}

	/**
	 * 以license文件方式初始化
	 */
	private void initAccessToken() {
		OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
			@Override
			public void onResult(AccessToken accessToken) {
				String token = accessToken.getAccessToken();
				hasGotToken = true;
			}

			@Override
			public void onError(OCRError error) {
				error.printStackTrace();
				alertText("licence方式获取token失败", error.getMessage());
			}
		}, getApplicationContext());
	}

	/**
	 * 用明文ak，sk初始化
	 */
	private void initAccessTokenWithAkSk() {
		OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
			@Override
			public void onResult(AccessToken result) {
				String token = result.getAccessToken();
				hasGotToken = true;
			}

			@Override
			public void onError(OCRError error) {
				error.printStackTrace();
				alertText("AK，SK方式获取token失败", error.getMessage());
			}
		}, getApplicationContext(), "nsDQGfOUURACenvSUUMGcGRr", "brWRZQvj51Bk8TyBo0svn8suQh0zKClO");
	}

	/**
	 * 自定义license的文件路径和文件名称，以license文件方式初始化
	 */
	private void initAccessTokenLicenseFile() {
		OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
			@Override
			public void onResult(AccessToken accessToken) {
				String token = accessToken.getAccessToken();
				hasGotToken = true;
			}

			@Override
			public void onError(OCRError error) {
				error.printStackTrace();
				alertText("自定义文件路径licence方式获取token失败", error.getMessage());
			}
		}, "aip.license", getApplicationContext());
	}


	private void alertText(final String title, final String message) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				alertDialog.setTitle(title)
						.setMessage(message)
						.setPositiveButton("确定", null)
						.show();
			}
		});
	}

	private void infoPopText(final String result) {
		alertText("", result);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			initAccessToken();
		} else {
			Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onNotifyContent(NotifyContentEvent contentEvent) {
		mTextView.setText(contentEvent.getContent());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 释放内存资源
	}

}
