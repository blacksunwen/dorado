package com.bstek.dorado.data.method;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-21
 */
public class MoreThanOneMethodsMatchsException extends
		MethodAutoMatchingException {
	private static final long serialVersionUID = 1L;

	public MoreThanOneMethodsMatchsException(String message) {
		super(message);
	}

}
