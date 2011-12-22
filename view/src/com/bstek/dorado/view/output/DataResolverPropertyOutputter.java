package com.bstek.dorado.view.output;

import com.bstek.dorado.data.resolver.DataResolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since May 14, 2009
 */
public class DataResolverPropertyOutputter extends ObjectPropertyOutputter {

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		DataResolver dataResolver = (DataResolver) object;
		JsonBuilder json = context.getJsonBuilder();
		json.beginValue();
		context.getWriter().write(
				"dorado.DataResolver.create(\"" + dataResolver.getId() + "\")");
		json.endValue();
	}
}
