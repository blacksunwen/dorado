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

package com.bstek.dorado.view.type.property;


import com.bstek.dorado.data.type.property.CacheMode;
import com.bstek.dorado.data.type.property.LazyPropertyDef;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.VirtualPropertyOutputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-21
 */
public class CacheModePropertyOutputter implements VirtualPropertyOutputter {

	public void output(Object object, String property, OutputContext context)
			throws Exception {
		CacheMode cacheMode = ((LazyPropertyDef) object).getCacheMode();
		if (CacheMode.isCacheableAtClientSide(cacheMode)) {
			context.getJsonBuilder().key("cacheable").value(true);
		}
	}

}
