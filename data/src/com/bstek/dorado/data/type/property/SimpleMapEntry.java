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

package com.bstek.dorado.data.type.property;


/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2015-2-12
 */
public class SimpleMapEntry {
	private Object key;
	private Object value;

	public SimpleMapEntry() {
	}

	public SimpleMapEntry(Object key, Object value) {
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public static SimpleMapEntry parseString(String s) {
		if (s == null) {
			return null;
		}
		int i = s.indexOf('=');
		if (i >= 0) {
			return new SimpleMapEntry(s.substring(0, i), s.substring(i + 1));
		} else {
			return new SimpleMapEntry(s, s);
		}
	}
}
