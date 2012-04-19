package com.bstek.dorado.data.type.property;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-20
 */
@ClientObject(properties = @ClientProperty(propertyName = "cacheable",
		outputter = "spring:dorado.cacheModePropertyOutputter"))
public abstract class LazyPropertyDef extends PropertyDefSupport {
	private boolean activeAtClient = true;
	private CacheMode cacheMode = CacheMode.bothSides;

	@ClientProperty(escapeValue = "true")
	public boolean isActiveAtClient() {
		return activeAtClient;
	}

	public void setActiveAtClient(boolean activeAtClient) {
		this.activeAtClient = activeAtClient;
	}

	@ClientProperty(escapeValue = "bothSides")
	public CacheMode getCacheMode() {
		return cacheMode;
	}

	public void setCacheMode(CacheMode cacheMode) {
		this.cacheMode = cacheMode;
	}
}
