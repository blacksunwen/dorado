package com.bstek.dorado.config.text;

import com.bstek.dorado.config.ParseContext;

/**
 * 解析字符串时使用的上下文对象。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 28, 2008
 */
public class TextParseContext extends ParseContext {
	private int currentIndex;

	/**
	 * 返回当前的解析位置。即将要从字符数组中的何处开始解析。
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * 设置当前的解析位置。即将要从字符数组中的何处开始解析。
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * 当前的解析位置加一。
	 */
	public void increaseCurrentIndex() {
		this.currentIndex++;
	}
}
