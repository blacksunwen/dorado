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

package com.bstek.dorado.web.loader;

import java.io.Writer;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2013-1-22
 */
public abstract class ConsoleStartedMessageOutputter implements
		Comparable<ConsoleStartedMessageOutputter> {
	private int order;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public abstract void output(Writer writer) throws Exception;

	public int compareTo(ConsoleStartedMessageOutputter o) {
		return o.getOrder() - order;
	}
}
