package com.bstek.dorado.view.output;

import com.bstek.dorado.common.StringAliasUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-29
 */
public class StringAliasPropertyOutputter implements PropertyOutputter {

	public boolean isEscapeValue(Object value) {
		return OutputUtils.isEscapeValue(value);
	}

	public void output(Object object, OutputContext context) throws Exception {
		String s = (String) object;
		context.getJsonBuilder().value(StringAliasUtils.getUniqueAlias(s));
	}

}
