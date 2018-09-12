package com.baidu.ocr.demo;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by chengjuzhang on 2018/9/10.
 */

public class MessageManager {
	private static volatile MessageManager INSTANCE = new MessageManager();
	private Observable mObservable;


	public static MessageManager getInstance() {
		return INSTANCE;
	}

	private MessageManager() {
		mObservable = new Observable();
	}


	public void addObserver(Observer observer) {
		mObservable.addObserver(observer);
	}

	public void removeObserver(Observer observer) {
		mObservable.deleteObserver(observer);
	}

	public void refreshNewOrder() {
		mObservable.notifyObservers();
	}


}
