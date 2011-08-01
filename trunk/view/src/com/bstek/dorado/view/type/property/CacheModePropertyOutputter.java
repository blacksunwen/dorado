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
