package com.bstek.dorado.core.el;

import java.util.Collections;

import com.bstek.dorado.util.proxy.UnmutableMap;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jun 24, 2009
 */
public abstract class VarMap extends UnmutableMap<String, Object> {
	@SuppressWarnings("unchecked")
	public VarMap() {
		super(Collections.EMPTY_MAP);
	}

	@Override
	public boolean containsKey(Object key) {
		return true;
	}
}
