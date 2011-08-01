package com.bstek.dorado.view.type.property.validator;

import java.text.MessageFormat;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
public class ValidateException extends IllegalArgumentException {

	private static final long serialVersionUID = -3227870431071116472L;

	public ValidateException() {
		super();
	}

	public ValidateException(String s) {
		super(s);
	}

	public ValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidateException(Throwable cause) {
		super(cause);
	}

	public ValidateException(String pattern, Object... arguments) {
		super(MessageFormat.format(pattern, arguments));
	}
}
