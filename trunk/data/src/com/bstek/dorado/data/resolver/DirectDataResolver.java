package com.bstek.dorado.data.resolver;

import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 29, 2009
 */
@XmlNode(fixedProperties = "type=direct")
public class DirectDataResolver extends AbstractDataResolver {

	@Override
	protected Object internalResolve(DataItems dataItems, Object parameter)
			throws Exception {
		return null;
	}

}
