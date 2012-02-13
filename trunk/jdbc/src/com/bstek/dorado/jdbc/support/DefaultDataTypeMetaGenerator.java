package com.bstek.dorado.jdbc.support;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.config.xml.DomHelper;
import com.bstek.dorado.jdbc.meta.DataTypeMetaGenerator;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public class DefaultDataTypeMetaGenerator implements DataTypeMetaGenerator {

	@Override
	public Document createDocument(JdbcEnviroment jdbcEnv, String tableName) {
		Document document = DomHelper.newDocument();
		Element rootElement = document.createElement("DataType");
		document.appendChild(rootElement);
		
		rootElement.setAttribute("name", tableName);
		
		DbTable table = getDbTable(tableName);
		List<Column> columns = table.getAllColumns();
		for (Column column: columns) {
			Element propertyDef = createPropertyDefElement(column, document);
			rootElement.appendChild(propertyDef);
		}
		
		return document;
	}

	@Override
	public Document mergeDocument(JdbcEnviroment jdbcEnv, String tableName,
			Document document) {
		Element rootElement = document.getDocumentElement();
		
		List<Element> propertyDefList = DomUtils.getChildElementsByTagName(rootElement, "PropertyDef");
		Set<String> propertyNameSet = new HashSet<String>();
		for (Element e: propertyDefList) {
			String propertyName = e.getAttribute("name");
			propertyNameSet.add(propertyName);
		}
		
		DbTable table = getDbTable(tableName);
		List<Column> columns = table.getAllColumns();
		for (Column column: columns) {
			Element propertyDef = createPropertyDefElement(column, document);
			String propertyName = propertyDef.getAttribute("name");
			if (!propertyNameSet.contains(propertyName)) {
				rootElement.appendChild(propertyDef);
			}
		}
		
		return document;
	}

	protected Element createPropertyDefElement(Column column, Document document) {
		String columnName = column.getColumnName();
		String propertyName = column.getPropertyName();
		Assert.notEmpty(propertyName, "propertyName of Column named [" + columnName +"] must not be empty.");
		
		Element propertyDef = document.createElement("PropertyDef");
		propertyDef.setAttribute("name", propertyName);
		JdbcType jdbcType = column.getJdbcType();
		if (jdbcType != null) {
			DataType dataType = jdbcType.getDataType();
			if (dataType != null) {
				Element dataTypeProperty = document.createElement("Property");
				propertyDef.appendChild(dataTypeProperty);
				dataTypeProperty.setAttribute("name", "dataType");
				dataTypeProperty.setTextContent(dataType.getName());
			}
		}
		
		return propertyDef;
	}
	
	protected DbTable getDbTable(String tableName) {
		DbElement dbe = JdbcUtils.getDbTable(tableName);
		Assert.isTrue(dbe instanceof DbTable, "[" + tableName + "] is not a table.");
		
		DbTable table = (DbTable) dbe;
		
		return table;
	}
}
