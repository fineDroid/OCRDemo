package com.baidu.ocr.demo.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangchengju
 * 主要功能:
 * 创建日期 2018/9/11
 * 作者:longxian
 */
public class SignalDataModel implements Serializable {
	private long log_id;
	private int direction;
	private int words_result_num;
	private List<WordResult> words_result;

	public long getLog_id() {
		return log_id;
	}

	public void setLog_id(long log_id) {
		this.log_id = log_id;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getWords_result_num() {
		return words_result_num;
	}

	public void setWords_result_num(int words_result_num) {
		this.words_result_num = words_result_num;
	}

	public List<WordResult> getWords_result() {
		return words_result;
	}

	public void setWords_result(List<WordResult> words_result) {
		this.words_result = words_result;
	}

	@Override
	public String toString() {
		return "SignalDataModel{" +
				"log_id=" + log_id +
				", direction=" + direction +
				", words_result_num=" + words_result_num +
				", words_result=" + words_result +
				'}';
	}

	public static class WordResult implements Serializable {

		private String words;

		public String getWords() {
			return words;
		}

		public void setWords(String words) {
			this.words = words;
		}

		@Override
		public String toString() {
			return "WordResult{" +
					"words='" + words + '\'' +
					'}';
		}
	}
}
