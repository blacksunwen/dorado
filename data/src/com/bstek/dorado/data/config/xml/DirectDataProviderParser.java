package com.bstek.dorado.data.config.xml;

import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.XmlParserUtils;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;

/**
 * 直接型DataProvider的专用解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 14, 2008
 */
public class DirectDataProviderParser extends DataProviderParser {

	@Override
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		super.initDefinition(definition, element, context);

		DataParseContext dataContext = (DataParseContext) context;
		Map<String, Object> properties = definition.getProperties();
		DefinitionReference<DataTypeDefinition> dataTypeRef = dataObjectParseHelper
				.getReferencedDataType(DataXmlConstants.ATTRIBUTE_RESULT_TYPE,
						DataXmlConstants.RESULT_TYPE, element, dataContext);
		if (dataTypeRef != null) {
			properties.put(DataXmlConstants.ATTRIBUTE_RESULT_TYPE, dataTypeRef);
		}

		Node resultNode = XmlParserUtils.getPropertyNode(element,
				DataXmlConstants.ATTRIBUTE_RESULT, DataXmlConstants.RESULT);
		if (resultNode != null) {
			dataContext.setCurrentDataType(dataTypeRef);
			Object result = dataParser.parse(resultNode, context);
			properties.put(DataXmlConstants.ATTRIBUTE_RESULT, result);
			dataContext.restoreCurrentDataType();
		}
	}
}
