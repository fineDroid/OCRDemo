package com.baidu.ocr.demo.data;

/**
 * @author zhangchengju
 * 主要功能:
 * 创建日期 2018/9/12
 * 作者:longxian
 */
public class WarningModel {
	private boolean isNeedWarning;
	private String content;

	public WarningModel(boolean isNeedWarning, String content) {
		this.isNeedWarning = isNeedWarning;
		this.content = content;
	}

	public boolean isNeedWarning() {
		return isNeedWarning;
	}

	public void setNeedWarning(boolean needWarning) {
		isNeedWarning = needWarning;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
