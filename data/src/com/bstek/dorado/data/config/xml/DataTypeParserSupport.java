package com.bstek.dorado.data.config.xml;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;

/**
 * DataType解析器的抽象类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 7, 2008
 */
public abstract class DataTypeParserSupport extends DataObjectParser {
	private static String DEFAULT_DATATYPE_PARENT = "Entity";

	public DataTypeParserSupport() {
		setScopable(true);
		setInheritable(true);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected DefinitionReference<DataTypeDefinition>[] getParentDefinitionReferences(
			String parentNameText, ParseContext context) throws Exception {
		DefinitionReference<DataTypeDefinition>[] parentReferences;
		String[] parentNames = StringUtils.split(parentNameText, ',');
		parentReferences = new DefinitionReference[parentNames.length];

		DataParseContext dataContext = (DataParseContext) context;
		for (int i = 0; i < parentNames.length; i++) {
			dataContext
					.setPrivateObjectNameSection(DataXmlConstants.PATH_PROPERTY_PREFIX
							+ XmlConstants.ATTRIBUTE_PARENT + (i + 1));
			try {
				String parentName = parentNames[i];
				DefinitionReference<DataTypeDefinition> dataTypeRef = dataObjectParseHelper
						.getDataTypeByName(parentName, dataContext, true);
				parentReferences[i] = dataTypeRef;
			} finally {
				dataContext.restorePrivateObjectName();
			}
		}
		return parentReferences;
	}

	@Override
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		if (StringUtils.isEmpty(element
				.getAttribute(XmlConstants.ATTRIBUTE_PARENT))) {
			element.setAttribute(XmlConstants.ATTRIBUTE_PARENT,
					DEFAULT_DATATYPE_PARENT);
		}

		DataTypeDefinition dataType = (DataTypeDefinition) definition;
		super.initDefinition(dataType, element, context);

		DataParseContext dataContext = (DataParseContext) context;
		Map<String, Object> properties = dataType.getProperties();
		DefinitionReference<DataTypeDefinition> dataTypeRef;
		dataTypeRef = dataObjectParseHelper.getReferencedDataType(
				DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE,
				DataXmlConstants.ELEMENT_DATA_TYPE, element, dataContext);
		if (dataTypeRef != null) {
			properties.put(DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE,
					dataTypeRef);
		}

		dataTypeRef = dataObjectParseHelper.getReferencedDataType(
				DataXmlConstants.ATTRIBUTE_KEY_DATA_TYPE,
				DataXmlConstants.KEY_DATA_TYPE, element, dataContext);
		if (dataTypeRef != null) {
			properties.put(DataXmlConstants.ATTRIBUTE_KEY_DATA_TYPE,
					dataTypeRef);
		}

		dataTypeRef = dataObjectParseHelper.getReferencedDataType(
				DataXmlConstants.ATTRIBUTE_VALUE_DATA_TYPE,
				DataXmlConstants.VALUE_DATA_TYPE, element, dataContext);
		if (dataTypeRef != null) {
			properties.put(DataXmlConstants.ATTRIBUTE_VALUE_DATA_TYPE,
					dataTypeRef);
		}
	}

}
