package com.bstek.dorado.data.config.xml;

import java.util.Map;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefaultDefinitionReference;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.config.DataTypeName;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;

/**
 * 内部DataType(私有DataType)的解析分派器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 9, 2008
 */
public class InnerDataTypeParser extends DataTypeParserSupport {

	@SuppressWarnings("unchecked")
	@Override
	protected Object internalParse(Node node, ParseContext context)
			throws Exception {
		DataTypeDefinition dataType = (DataTypeDefinition) super.internalParse(
				node, context);

		DataParseContext dataContext = (DataParseContext) context;
		String name = dataContext.getPrivateObjectName();

		// 处理[Bean]这类数据类型的特殊情况
		Map<String, DataTypeDefinition> parsedDataTypes = dataContext
				.getParsedDataTypes();
		if (dataType.getParentReferences().length == 1) {
			DefaultDefinitionReference<?> parentReference = (DefaultDefinitionReference<?>) dataType
					.getParentReferences()[0];
			DataTypeName dataTypeName = new DataTypeName(
					parentReference.getName());
			if (dataTypeName.getSubDataTypes().length == 1) {
				String elementDataTypeName = name
						+ DataXmlConstants.PATH_PROPERTY_PREFIX
						+ DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE;
				name = ((Constants.DEFAULT_COLLECTION_TYPE.equals(dataTypeName
						.getDataType())) ? "" : dataTypeName.getDataType())
						+ '[' + elementDataTypeName + ']';

				DataTypeDefinition elementDataType = dataType;
				elementDataType.setName(elementDataTypeName);
				elementDataType.setId(dataContext.getDataObjectIdPrefix()
						+ elementDataTypeName);
				elementDataType
						.setParentReferences(new DefinitionReference[] { dataObjectParseHelper
								.getDataTypeByName(
										dataTypeName.getSubDataTypes()[0],
										dataContext, true) });
				parsedDataTypes.put(elementDataTypeName, elementDataType);

				dataType = new DataTypeDefinition();
				dataType.setResource(elementDataType.getResource());
				dataType.setGlobal(false);
				DefinitionReference<?> dataTypeRef = dataContext
						.getDataTypeReference(dataTypeName.getDataType());
				dataType.setParentReferences(new DefinitionReference[] { dataTypeRef });
				dataType.getProperties().put(
						DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE,
						elementDataType);
			}
		}

		dataType.setName(name);
		dataType.setId(dataContext.getDataObjectIdPrefix() + name);

		parsedDataTypes.put(name, dataType);
		return dataType;
	}
}
