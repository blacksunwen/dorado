﻿/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.data.config.xml;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;

/**
 * DataProvider解析器的抽象类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 2, 2007
 */
public class DataProviderParser extends GenericObjectParser {

	@Override
	@SuppressWarnings("unchecked")
	protected DefinitionReference<DataProviderDefinition>[] getParentDefinitionReferences(
			String parentNameText, ParseContext context) throws Exception {
		DefinitionReference<DataProviderDefinition> parentReference = ((DataParseContext) context)
				.getDataProviderReference(parentNameText);
		if (parentReference != null) {
			return new DefinitionReference[] { parentReference };
		} else {
			return null;
		}
	}

	@Override
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		super.initDefinition(definition, element, context);

		DataProviderDefinition dataProvider = (DataProviderDefinition) definition;

		String interceptor = (String) dataProvider.getProperties().remove(
				XmlConstants.ATTRIBUTE_INTERCEPTOR);
		if (StringUtils.isNotEmpty(interceptor)) {
			dataProvider.setInterceptor(interceptor);
		}

		DefinitionReference<DataTypeDefinition> dataTypeRef = dataObjectParseHelper
				.getReferencedDataType(
						DataXmlConstants.ATTRIBUTE_RESULT_DATA_TYPE,
						DataXmlConstants.RESULT_TYPE, element,
						(DataParseContext) context);
		if (dataTypeRef != null) {
			definition.getProperties().put(
					DataXmlConstants.ATTRIBUTE_RESULT_DATA_TYPE, dataTypeRef);
		}
	}
}
