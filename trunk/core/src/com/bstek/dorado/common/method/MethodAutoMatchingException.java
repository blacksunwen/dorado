package com.bstek.dorado.common.method;

/**
 * 当{@link com.bstek.dorado.common.method.MethodAutoMatchingUtils}
 * 找到了一个以上的匹配方法或没有找到任何匹配的方法将抛出的异常
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 3, 2008
 */
public class MethodAutoMatchingException extends Exception {
	private static final long serialVersionUID = -1237977702030591321L;

	/**
	 * @param message 错误信息
	 */
	public MethodAutoMatchingException(String message) {
		super(message);
	}

}
