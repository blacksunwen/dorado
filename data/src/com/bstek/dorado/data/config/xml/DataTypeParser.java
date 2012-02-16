package com.bstek.dorado.data.config.xml;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.config.xml.ObjectParserInitializationAware;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.config.xml.XmlParseException;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.config.DataTypeName;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionReference;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.util.clazz.ClassUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-11-17
 */
public class DataTypeParser extends GenericObjectParser implements
		ObjectParserInitializationAware {
	private static String DEFAULT_DATATYPE_PARENT = "Entity";

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

	@SuppressWarnings("unchecked")
	@Override
	protected Object internalParse(Node node, ParseContext context)
			throws Exception {
		Element element = (Element) node;
		DataParseContext dataContext = (DataParseContext) context;
		Set<Node> parsingNodes = dataContext.getParsingNodes();
		Map<String, DataTypeDefinition> parsedDataTypes = dataContext
				.getParsedDataTypes();

		String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
		NodeWrapper nodeWrapper = null;
		if (!StringUtils.isEmpty(name)) {
			nodeWrapper = dataContext.getConfiguredDataTypes().get(name);
		}
		boolean isGlobal = (nodeWrapper != null && nodeWrapper.getNode() == node);

		DataTypeDefinition dataType = null;
		if (!StringUtils.isEmpty(name) && isGlobal) {
			// Comment 11/04/26 为了处理View中私有DataObject与Global DataObject重名的问题
			// DefinitionManager<DataTypeDefinition> dataTypeDefinitionManager =
			// dataContext
			// .getDataTypeDefinitionManager();
			// dataType = dataTypeDefinitionManager.getDefinition(name);
			dataType = parsedDataTypes.get(name);
			if (dataType != null) {
				return dataType;
			}
		}

		if (isGlobal) {
			parsingNodes.add(element);
			dataContext
					.setPrivateObjectName(Constants.PRIVATE_DATA_OBJECT_PREFIX
							+ DataXmlConstants.PATH_DATE_TYPE_SHORT_NAME
							+ Constants.PRIVATE_DATA_OBJECT_SUBFIX + name);

			dataType = (DataTypeDefinition) super.internalParse(node,
					dataContext);

			Class<?> matchType = (Class<?>) dataType.getProperties().remove(
					DataXmlConstants.ATTRIBUTE_MATCH_TYPE);
			dataType.setMatchType(matchType);

			Class<?> creationType = (Class<?>) dataType.getProperties().remove(
					DataXmlConstants.ATTRIBUTE_CREATION_TYPE);
			if (creationType != null) {
				if (matchType != null
						&& !matchType.isAssignableFrom(creationType)) {
					throw new XmlParseException("The CreationType ["
							+ creationType
							+ "] is not a sub type of the MatchType ["
							+ matchType + "].", element, context);
				}
				dataType.setCreationType(creationType);
			}

			dataContext.restorePrivateObjectName();
			parsingNodes.clear();
		} else {
			name = dataContext.getPrivateObjectName();

			dataType = (DataTypeDefinition) super.internalParse(node,
					dataContext);

			// 处理[Bean]这类数据类型的特殊情况
			if (dataType.getParentReferences() != null
					&& dataType.getParentReferences().length == 1) {
				DataTypeDefinitionReference parentReference = (DataTypeDefinitionReference) dataType
						.getParentReferences()[0];
				DataTypeName dataTypeName = new DataTypeName(
						parentReference.getName());
				if (dataTypeName.getSubDataTypes().length == 1) {
					String elementDataTypeName = name
							+ DataXmlConstants.PATH_PROPERTY_PREFIX
							+ DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE;
					name = ((Constants.DEFAULT_COLLECTION_TYPE
							.equals(dataTypeName.getDataType())) ? ""
							: dataTypeName.getDataType())
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
		}

		dataType.setName(name);
		dataType.setId(dataContext.getDataObjectIdPrefix() + name);
		dataType.setGlobal(isGlobal);

		if (dataType.getParentReferences() == null
				&& !DEFAULT_DATATYPE_PARENT.equals(name)) {
			boolean useDefaultParent = false;
			String impl = dataType.getImpl();
			if (StringUtils.isNotEmpty(impl)) {
				Class<? extends DataType> type = ClassUtils.forName(impl);
				useDefaultParent = EntityDataType.class.isAssignableFrom(type);
			} else {
				useDefaultParent = true;
			}

			if (useDefaultParent) {
				DefinitionReference<?> dataTypeRef = dataContext
						.getDataTypeReference(DEFAULT_DATATYPE_PARENT);
				dataType.setParentReferences(new DefinitionReference[] { dataTypeRef });
			}
		}

		parsedDataTypes.put(name, dataType);
		return dataType;
	}

	public void postObjectParserInitialized(ObjectParser objectParser)
			throws Exception {
		setImpl(null);
	}
}
