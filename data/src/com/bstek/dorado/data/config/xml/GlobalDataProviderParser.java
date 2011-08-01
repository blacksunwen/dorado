package com.bstek.dorado.data.config.xml;

import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.util.Assert;

/**
 * 全局DataProvider的解析分派器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 13, 2008
 */
public class GlobalDataProviderParser extends DataProviderParserDispatcher {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Element element = (Element) node;
		DataParseContext dataContext = (DataParseContext) context;
		Set<Node> parsingNodes = dataContext.getParsingNodes();
		Map<String, DataProviderDefinition> parsedDataProviders = dataContext
				.getParsedDataProviders();

		String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
		Assert.notEmpty(name);

		// Comment 11/04/26 为了处理View中私有DataObject与Global DataObject重名的问题
		// DefinitionManager<DataProviderDefinition>
		// dataProviderDefinitionManager = dataContext
		// .getDataProviderDefinitionManager();
		// DataProviderDefinition dataProvider = dataProviderDefinitionManager
		// .getDefinition(name);
		// if (dataProvider == null) {
		// dataProvider = parsedDataProviders.get(name);
		// }
		DataProviderDefinition dataProvider = parsedDataProviders.get(name);

		if (dataProvider == null) {
			parsingNodes.add(element);
			dataContext
					.setPrivateObjectName(Constants.PRIVATE_DATA_OBJECT_PREFIX
							+ DataXmlConstants.PATH_DATE_PROVIDER_SHORT_NAME
							+ Constants.PRIVATE_DATA_OBJECT_SUBFIX + name);

			dataProvider = (DataProviderDefinition) super.doParse(node,
					dataContext);

			dataProvider.setName(name);
			dataProvider.setId(dataContext.getDataObjectIdPrefix() + name);
			dataProvider.setGlobal(true);

			dataContext.restorePrivateObjectName();
			parsingNodes.clear();
			dataContext.getParsedDataProviders().put(name, dataProvider);
		}
		return dataProvider;
	}
}
