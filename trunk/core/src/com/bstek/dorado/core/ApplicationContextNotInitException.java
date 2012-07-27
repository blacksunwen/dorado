package com.bstek.dorado.core;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-7-27
 */
public class ApplicationContextNotInitException extends RuntimeException {
	private static final long serialVersionUID = 965700099577319911L;

	public ApplicationContextNotInitException(String message) {
		super(message);
	}

}
