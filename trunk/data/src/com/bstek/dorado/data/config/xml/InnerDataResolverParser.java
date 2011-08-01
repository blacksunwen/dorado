package com.bstek.dorado.data.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.data.config.definition.DataResolverDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 29, 2009
 */
public class InnerDataResolverParser extends DataResolverParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		DataParseContext dataContext = (DataParseContext) context;
		String privateNameSection = dataContext.getPrivateNameSection(node);
		if (privateNameSection != null) {
			dataContext.setPrivateObjectNameSection(privateNameSection);
		}

		DataResolverDefinition dataResolver = (DataResolverDefinition) super
				.doParse(node, context);
		String name = dataContext.getPrivateObjectName();
		dataResolver.setName(name);
		dataResolver.setId(dataContext.getDataObjectIdPrefix() + name);

		if (privateNameSection != null) {
			dataContext.restorePrivateObjectName();
		}
		dataContext.getParsedDataResolvers().put(name, dataResolver);
		return dataResolver;
	}
}
