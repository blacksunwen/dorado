/**
 * 
 */
package com.bstek.dorado.data;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-10-15
 */
public class IllegalJsonFormatException extends RuntimeException {
	private static final long serialVersionUID = 5987781189952481383L;

	public IllegalJsonFormatException() {
		super();
	}

	public IllegalJsonFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalJsonFormatException(String message) {
		super(message);
	}

	public IllegalJsonFormatException(Throwable cause) {
		super(cause);
	}

}
