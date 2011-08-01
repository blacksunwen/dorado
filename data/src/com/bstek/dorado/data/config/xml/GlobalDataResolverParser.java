package com.bstek.dorado.data.config.xml;

import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.config.definition.DataResolverDefinition;
import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 29, 2009
 */
public class GlobalDataResolverParser extends DataResolverParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Element element = (Element) node;
		DataParseContext dataContext = (DataParseContext) context;
		Set<Node> parsingNodes = dataContext.getParsingNodes();
		Map<String, DataResolverDefinition> parsedDataResolvers = dataContext
				.getParsedDataResolvers();

		String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
		Assert.notEmpty(name);

		// Comment 11/04/26 为了处理View中私有DataObject与Global DataObject重名的问题
		// DefinitionManager<DataResolverDefinition>
		// dataResolverDefinitionManager = dataContext
		// .getDataResolverDefinitionManager();
		// DataResolverDefinition dataResolver = dataResolverDefinitionManager
		// .getDefinition(name);
		// if (dataResolver == null) {
		// dataResolver = parsedDataResolvers.get(name);
		// }
		DataResolverDefinition dataResolver = parsedDataResolvers.get(name);

		if (dataResolver == null) {
			parsingNodes.add(element);
			dataContext
					.setPrivateObjectName(Constants.PRIVATE_DATA_OBJECT_PREFIX
							+ DataXmlConstants.PATH_DATE_PROVIDER_SHORT_NAME
							+ Constants.PRIVATE_DATA_OBJECT_SUBFIX + name);

			dataResolver = (DataResolverDefinition) super.doParse(node,
					dataContext);

			dataResolver.setName(name);
			dataResolver.setId(dataContext.getDataObjectIdPrefix() + name);
			dataResolver.setGlobal(true);

			dataContext.restorePrivateObjectName();
			parsingNodes.clear();
			dataContext.getParsedDataResolvers().put(name, dataResolver);
		}
		return dataResolver;
	}
}
