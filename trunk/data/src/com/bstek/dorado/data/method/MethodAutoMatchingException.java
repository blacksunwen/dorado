/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.data.method;

/**
 * 当{@link com.bstek.dorado.data.method.MethodAutoMatchingUtils}
 * 找到了一个以上的匹配方法或没有找到任何匹配的方法将抛出的异常
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 3, 2008
 */
public class MethodAutoMatchingException extends Exception {
	private static final long serialVersionUID = -1237977702030591321L;

	private String header;
	private String detail;

	/**
	 * @param message
	 *            错误信息
	 */
	public MethodAutoMatchingException(String header, String detail) {
		super(header + detail);
		this.header = header;
		this.detail = detail;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
