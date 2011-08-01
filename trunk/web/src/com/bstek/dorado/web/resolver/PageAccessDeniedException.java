/**
 * 
 */
package com.bstek.dorado.web.resolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-18
 */
public class PageAccessDeniedException extends IllegalAccessException {
	private static final long serialVersionUID = -2047396921354715436L;

	public PageAccessDeniedException(String message) {
		super(message);
	}

	public PageAccessDeniedException(Throwable cause) {
		this(cause.getMessage());
		this.initCause(cause);
	}

	public PageAccessDeniedException(String message, Throwable cause) {
		this(message);
		this.initCause(cause);
	}
}
