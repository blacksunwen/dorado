package com.bstek.dorado.idesupport.robot;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.CommonContext;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.config.DataConfigManager;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.data.config.definition.DataResolverDefinitionManager;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.data.config.xml.DataParseContext;
import com.bstek.dorado.data.config.xml.DataXmlConstants;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-2
 */
@RobotInfo(viewObject = "DataType", label = "Generate PropertyDefs (Advance)")
public class EntityDataTypeReflectionRobot implements Robot {
	private static final Set<String> IGNORE_DATATYPES = new HashSet<String>();

	static {
		IGNORE_DATATYPES.add("Object");
		IGNORE_DATATYPES.add("Map");
		IGNORE_DATATYPES.add("Record");
	}

	public Node execute(Node node, Properties properties) throws Exception {
		RobotContext.init();
		try {
			Context context = Context.getCurrent();
			DataConfigManager dataConfigManager = (DataConfigManager) context
					.getServiceBean("dataConfigManager");
			dataConfigManager.initialize();

			XmlParser dataTypeParser = (XmlParser) context
					.getServiceBean("globalDataTypeParser");
			DataParseContext parseContext = new DataParseContext();
			parseContext
					.setDataTypeDefinitionManager((DataTypeDefinitionManager) context
							.getServiceBean("dataTypeDefinitionManager"));
			parseContext
					.setDataProviderDefinitionManager((DataProviderDefinitionManager) context
							.getServiceBean("dataProviderDefinitionManager"));
			parseContext
					.setDataResolverDefinitionManager((DataResolverDefinitionManager) context
							.getServiceBean("dataResolverDefinitionManager"));

			DataTypeDefinition dataTypeDefinition = (DataTypeDefinition) dataTypeParser
					.parse(node, parseContext);

			Class<?> matchType = dataTypeDefinition.getMatchType();
			if (matchType != null) {
				if (matchType == null || matchType.isPrimitive()
						|| matchType.isArray()) {
					throw new IllegalArgumentException(
							"[matchType] undefined or not a valid class.");
				}
				node = node.cloneNode(true);
				reflectAndComplete((Element) node, matchType);
			}
		} finally {
			CommonContext.dispose();
		}
		return node;
	}

	protected void reflectAndComplete(Element element, Class<?> cls)
			throws Exception {
		Context context = Context.getCurrent();
		DataTypeManager dataTypeManager = (DataTypeManager) context
				.getServiceBean("dataTypeManager");
		Document document = element.getOwnerDocument();

		Map<String, Element> propertyDefElementMap = new HashMap<String, Element>();
		for (Element propertyDefElement : DomUtils.getChildrenByTagName(
				element, DataXmlConstants.PROPERTY_DEF)) {
			String name = propertyDefElement
					.getAttribute(XmlConstants.ATTRIBUTE_NAME);
			propertyDefElementMap.put(name, propertyDefElement);
		}

		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(cls);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String name = propertyDescriptor.getName();
			if ("class".equals(name))
				continue;
			Element propertyDefElement = propertyDefElementMap.get(name);
			if (propertyDefElement == null) {
				String dataTypeName = null;

				DataType propertyDataType = dataTypeManager
						.getDataType(propertyDescriptor.getPropertyType());
				if (propertyDataType != null) {
					dataTypeName = propertyDataType.getName();
					if (IGNORE_DATATYPES.contains(dataTypeName)) {
						continue;
					}
				}

				propertyDefElement = document
						.createElement(DataXmlConstants.PROPERTY_DEF);
				propertyDefElement.setAttribute(XmlConstants.ATTRIBUTE_NAME,
						name);
				createPropertyElement(propertyDefElement,
						DataXmlConstants.ATTRIBUTE_DATA_TYPE, dataTypeName);
				element.appendChild(propertyDefElement);
			}
		}
	}

	private Element createPropertyElement(Element parentElement,
			String propertyName) {
		Document document = parentElement.getOwnerDocument();
		Element propertyElement = document.createElement("Property");
		propertyElement.setAttribute(XmlConstants.ATTRIBUTE_NAME, propertyName);
		parentElement.appendChild(propertyElement);
		return propertyElement;
	}

	private Element createPropertyElement(Element parentElement,
			String propertyName, String propertyValue) {
		Element propertyElement = createPropertyElement(parentElement,
				propertyName);
		propertyElement.setTextContent(propertyValue);
		return propertyElement;
	}
}
