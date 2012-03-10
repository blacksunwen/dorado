package com.bstek.dorado.jdbc.support;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.data.config.xml.DataXmlConstants;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.jdbc.JdbcOperationUtils;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.meta.DataTypeMetaDataGenerator;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.type.JdbcType;

public class DefaultDataTypeMetaDataGenerator implements DataTypeMetaDataGenerator {

	@Override
	public Document create(String tableName) {
		Document document = DomHelper.newDocument();
		Element rootElement = document.createElement(DataXmlConstants.DATA_TYPE);
		document.appendChild(rootElement);
		
		rootElement.setAttribute(XmlConstants.ATTRIBUTE_NAME, tableName);
		
		DbTable table = JdbcOperationUtils.getDbTable(tableName);
		List<AbstractDbColumn> columns = table.getAllColumns();
		for (AbstractDbColumn column: columns) {
			Element propertyDef = createPropertyDefElement(column, document);
			rootElement.appendChild(propertyDef);
		}
		
		return document;
	}

	@Override
	public Document merge(String tableName, Document document) {
		Element rootElement = document.getDocumentElement();
		rootElement.setAttribute(XmlConstants.ATTRIBUTE_NAME, tableName);
		
		List<Element> propertyDefList = DomUtils.getChildElementsByTagName(rootElement, DataXmlConstants.PROPERTY_DEF);
		Set<String> propertyNameSet = new HashSet<String>();
		for (Element e: propertyDefList) {
			String propertyName = e.getAttribute(XmlConstants.ATTRIBUTE_NAME);
			propertyNameSet.add(propertyName);
		}
		
		DbTable table = JdbcOperationUtils.getDbTable(tableName);
		List<AbstractDbColumn> columns = table.getAllColumns();
		for (AbstractDbColumn column: columns) {
			Element propertyDef = createPropertyDefElement(column, document);
			String propertyName = propertyDef.getAttribute(XmlConstants.ATTRIBUTE_NAME);
			if (!propertyNameSet.contains(propertyName)) {
				rootElement.appendChild(propertyDef);
			}
		}
		
		return document;
	}

	protected Element createPropertyDefElement(AbstractDbColumn column, Document document) {
		String propertyName = column.getPropertyName();
		if (StringUtils.isNotEmpty(propertyName)) {
			Element propertyDef = document.createElement(DataXmlConstants.PROPERTY_DEF);
			propertyDef.setAttribute(XmlConstants.ATTRIBUTE_NAME, propertyName);
			JdbcType jdbcType = column.getJdbcType();
			if (jdbcType != null) {
				DataType dataType = jdbcType.getDataType();
				if (dataType != null) {
					Element dataTypeProperty = document.createElement(XmlConstants.PROPERTY);
					propertyDef.appendChild(dataTypeProperty);
					dataTypeProperty.setAttribute(XmlConstants.ATTRIBUTE_NAME, DataXmlConstants.ATTRIBUTE_DATA_TYPE);
					dataTypeProperty.setTextContent(dataType.getName());
				}
			}
			
			return propertyDef;
		} else {
			return null;
		}
	}
	
}
