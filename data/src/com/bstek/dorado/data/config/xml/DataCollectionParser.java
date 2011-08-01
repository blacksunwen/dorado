package com.bstek.dorado.data.config.xml;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;

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
		this.collectionType = ClassUtils.getClass(collectionType);
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
					.getProperties().get(
							DataXmlConstants.ATTRIBUTE_ELEMENT_DATA_TYPE);
		}
		context.setCurrentDataType(dataTypeRef);

		Collection<Object> collection = createCollection();
		List<?> elements = super.dispatchChildElements(element, context);
		collection.addAll(elements);

		context.restoreCurrentDataType();
		return collection;
	}
}
