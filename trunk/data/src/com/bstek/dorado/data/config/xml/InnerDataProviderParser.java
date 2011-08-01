package com.bstek.dorado.data.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;

/**
 * 内部DataProvider(私有DataProvider)的解析分派器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 13, 2008
 */
public class InnerDataProviderParser extends DataProviderParserDispatcher {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		DataParseContext dataContext = (DataParseContext) context;
		String privateNameSection = dataContext.getPrivateNameSection(node);
		if (privateNameSection != null) {
			dataContext.setPrivateObjectNameSection(privateNameSection);
		}

		DataProviderDefinition dataProvider = (DataProviderDefinition) super
				.doParse(node, context);
		String name = dataContext.getPrivateObjectName();
		dataProvider.setName(name);
		dataProvider.setId(dataContext.getDataObjectIdPrefix() + name);

		if (privateNameSection != null) {
			dataContext.restorePrivateObjectName();
		}
		dataContext.getParsedDataProviders().put(name, dataProvider);
		return dataProvider;
	}
}
