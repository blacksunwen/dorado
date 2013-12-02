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

import java.util.Collection;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.util.clazz.ClassUtils;

/**
 * 集合类对象实例的抽象解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 31, 2007
 */
public class DataCollectionParser extends DataElementParserSupport {
	private Class<Collection<Object>> collectionType;

	/**
	 * 设置具体的集合类实现类型。
	 * 
	 * @param collectionType
	 *            集合类实现类型，例如:java.util.ArrayList
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public void setCollectionType(String collectionType)
			throws ClassNotFoundException {
		this.collectionType = ClassUtils.forName(collectionType);
	}

	/**
	 * 创建具体的集合对象。
	 * 
	 * @throws Exception
	 */
	protected Collection<Object> createCollection() throws Exception {
		return collectionType.newInstance();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Object internalParse(Node node, DataParseContext context)
			throws Exception {
		Element element = (Element) node;

		DefinitionReference<DataTypeDefinition> dataTypeRef = dataObjectParseHelper
				.getReferencedDataType(DataXmlConstants.ATTRIBUTE_DATA_TYPE,
						null, element, context);
		if (dataTypeRef != null) {
			DataTypeDefinition dataTypeDefinition = dataTypeRef.getDefinition();
			dataTypeRef = (DefinitionReference<DataTypeDefinition>) dataTypeDefinition
					.getProperty(DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE);
		}
		context.setCurrentDataType(dataTypeRef);

		Collection<Object> collection = createCollection();
		List<?> elements = super.dispatchChildElements(element, context);
		collection.addAll(elements);

		context.restoreCurrentDataType();
		return collection;
	}
}
