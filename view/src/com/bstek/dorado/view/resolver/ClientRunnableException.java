/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.resolver;

import org.apache.commons.lang.StringUtils;

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

	public ClientRunnableException(String message, String script) {
		super(message);
		this.script = script;
	}

	public String getScript() {
		return script;
	}

	public String toString() {
		String message = getMessage();
		if (StringUtils.isEmpty(message)) {
			return script;
		} else {
			return super.toString();
		}
	}
}
