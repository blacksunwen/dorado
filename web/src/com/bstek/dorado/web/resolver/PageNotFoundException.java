/**
 * 
 */
package com.bstek.dorado.web.resolver;

import java.io.FileNotFoundException;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-18
 */
public class PageNotFoundException extends FileNotFoundException {
	private static final long serialVersionUID = -7660289430597209704L;

	public PageNotFoundException(String message) {
		super(message);
	}

	public PageNotFoundException(Throwable cause) {
		this(cause.getMessage());
		this.initCause(cause);
	}

	public PageNotFoundException(String message, Throwable cause) {
		this(message);
		this.initCause(cause);
	}
}
