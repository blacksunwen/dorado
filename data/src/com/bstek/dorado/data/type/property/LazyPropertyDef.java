package com.bstek.dorado.data.type.property;

import com.bstek.dorado.annotation.ViewAttribute;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-20
 */
public abstract class LazyPropertyDef extends PropertyDefSupport {
	private boolean activeAtClient = true;
	private CacheMode cacheMode = CacheMode.bothSides;

	@ViewAttribute(output = false)
	public boolean isActiveAtClient() {
		return activeAtClient;
	}

	public void setActiveAtClient(boolean activeAtClient) {
		this.activeAtClient = activeAtClient;
	}

	@ViewAttribute(output = false)
	public CacheMode getCacheMode() {
		return cacheMode;
	}

	public void setCacheMode(CacheMode cacheMode) {
		this.cacheMode = cacheMode;
	}
}
