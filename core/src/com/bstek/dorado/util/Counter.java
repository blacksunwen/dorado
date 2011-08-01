package com.bstek.dorado.util;

/**
 * 计数器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 3, 2008
 */
public class Counter {
	private int i = 0;

	/**
	 * 增加一个计数。
	 */
	public void increase() {
		i++;
	}

	/**
	 * 减少一个计数。
	 */
	public void decrease() {
		i--;
	}

	/**
	 * 直接设置计数值。
	 */
	public void setValue(int i) {
		this.i = i;
	}

	/**
	 * 返回计数值。
	 */
	public int getValue() {
		return i;
	}
}
