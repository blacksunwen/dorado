package com.bstek.dorado.view.resolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-18
 */
public class ClientRunnableException extends RuntimeException {
	private static final long serialVersionUID = 3478313367942463176L;
	private String script;

	public ClientRunnableException(String script) {
		this.script = script;
	}

	public String getScript() {
		return script;
	}
}
