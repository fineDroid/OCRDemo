package com.baidu.ocr.demo.task;

import java.io.Serializable;


public class NotifyContentEvent implements Serializable {
	private String content;

	public NotifyContentEvent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
