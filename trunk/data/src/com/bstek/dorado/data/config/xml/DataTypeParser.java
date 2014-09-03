/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.data.config.xml;

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
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.util.clazz.ClassUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-11-17
 */
public class DataTypeParser extends GenericObjectParser implements
		ObjectParserInitializationAware {
	private String defaultImpl;

	@Override
	@SuppressWarnings("unchecked")
	protected DefinitionReference<DataTypeDefinition>[] getParentDefinitionReferences(
			String parentNameText, ParseContext context) throws Exception {
		DefinitionReference<DataTypeDefinition>[] parentReferences;
		String[] parentNames = StringUtils.split(parentNameText, ',');
		parentReferences = new DefinitionReference[parentNames.length];

		DataParseContext dataContext = (DataParseContext) context;
		for (int i = 0; i < parentNames.length; i++) {
			String parentName = parentNames[i];
			DefinitionReference<DataTypeDefinition> dataTypeRef = dataObjectParseHelper
					.getDataTypeByName(parentName, dataContext, true);
			parentReferences[i] = dataTypeRef;
		}
		return parentReferences;
	}

	@Override
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		super.initDefinition(definition, element, context);

		definition.setDefaultImpl(ClassUtils.forName(defaultImpl));

		DataParseContext dataContext = (DataParseContext) context;
		DefinitionReference<DataTypeDefinition> dataTypeRef;
		dataTypeRef = dataObjectParseHelper.getReferencedDataType(
				DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE, element,
				dataContext);
		if (dataTypeRef != null) {
			definition.setProperty(
					DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE, dataTypeRef);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object internalParse(Node node, ParseContext context)
			throws Exception {
		Element element = (Element) node;
		DataTypeDefinition dataType = (DataTypeDefinition) super.internalParse(
				node, context);

		Class<?> matchType = (Class<?>) dataType
				.removeProperty(DataXmlConstants.ATTRIBUTE_MATCH_TYPE);
		dataType.setMatchType(matchType);

		Class<?> creationType = (Class<?>) dataType
				.removeProperty(DataXmlConstants.ATTRIBUTE_CREATION_TYPE);
		if (creationType != null) {
			if (matchType != null && !matchType.isAssignableFrom(creationType)) {
				throw new XmlParseException("The CreationType [" + creationType
						+ "] is not a sub type of the MatchType [" + matchType
						+ "].", element, context);
			}
			dataType.setCreationType(creationType);
		}

		final String DEFAULT_DATATYPE_PARENT = Configure.getString(
				"data.defaultEntityDataTypeParent", "Entity");

		String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
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
				DataParseContext dataContext = (DataParseContext) context;
				DefinitionReference<?> dataTypeRef = dataContext
						.getDataTypeReference(DEFAULT_DATATYPE_PARENT);
				dataType.setParentReferences(new DefinitionReference[] { dataTypeRef });
			}
		}
		return dataType;
	}

	public void postObjectParserInitialized(ObjectParser objectParser)
			throws Exception {
		defaultImpl = getImpl();
		setImpl(null);
	}
}
