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
package com.bstek.dorado.vidorsupport.plugin;


public class ConfigureParseException extends RuntimeException {

	private static final long serialVersionUID = 2384211675703650483L;

	public ConfigureParseException(){}
	
	public ConfigureParseException(Exception e) {
		super(e);
	}
	
	public ConfigureParseException(String message) {
		super(message);
	}
}
