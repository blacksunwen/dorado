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
