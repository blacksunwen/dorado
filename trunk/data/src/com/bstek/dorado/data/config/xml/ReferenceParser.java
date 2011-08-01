package com.bstek.dorado.data.config.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserUtils;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;

/**
 * EntityDataType中数据关联属性声明对象的解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apirl 22, 2007
 */
public class ReferenceParser extends PropertyDefParser {

	/**
	 * 用于解析各种数据节点的解析器。
	 */
	protected XmlParser dataParser;

	/**
	 * 设置用于解析各种数据节点的解析器。
	 */
	public void setDataParser(XmlParser dataParser) {
		this.dataParser = dataParser;
	}

	@Override
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		super.initDefinition(definition, element, context);

		DefinitionReference<DataProviderDefinition> dataProviderRef = dataObjectParseHelper
				.getReferencedDataProvider(
						DataXmlConstants.ATTRIBUTE_DATA_PROVIDER,
						DataXmlConstants.DATA_PROVIDER, element,
						(DataParseContext) context);
		if (dataProviderRef != null) {
			definition.getProperties().put(
					DataXmlConstants.ATTRIBUTE_DATA_PROVIDER, dataProviderRef);
		}

		Node parameterNode = XmlParserUtils.getPropertyNode(element,
				DataXmlConstants.ATTRIBUTE_PARAMETER,
				DataXmlConstants.PARAMETER);
		if (parameterNode != null) {
			Object parameter = dataParser.parse(parameterNode, context);
			definition.getProperties().put(
					DataXmlConstants.ATTRIBUTE_PARAMETER, parameter);
		}
	}
}
