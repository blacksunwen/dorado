package com.bstek.dorado.data.config.xml;

import org.w3c.dom.Element;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;

/**
 * EntityDataType中数据关联属性声明对象的解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apirl 22, 2007
 */
public class ReferenceParser extends PropertyDefParser {

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
	}
}
