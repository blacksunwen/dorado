package com.bstek.dorado.jdbc.support;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.meta.DataTypeMetaGenerator;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public class DefaultDataTypeMetaGenerator implements DataTypeMetaGenerator {

	@Override
	public Document createDocument(JdbcEnviroment jdbcEnv, String tableName) {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("DataType");
		rootElement.addAttribute("name", tableName);
		
		DbTable table = getDbTable(tableName);
		List<Column> columns = table.getAllColumns();
		for (Column column: columns) {
			Element propertyDef = createPropertyDefElement(column);
			rootElement.add(propertyDef);
		}
		
		return document;
	}

	@Override
	public Document mergeDocument(JdbcEnviroment jdbcEnv, String tableName,
			Document document) {
		Element rootElement = document.getRootElement();
		
		@SuppressWarnings("unchecked")
		List<Element> propertyDefList = rootElement.elements();
		Set<String> propertyNameSet = new HashSet<String>();
		for (Element e: propertyDefList) {
			String propertyName = e.attributeValue("name");
			propertyNameSet.add(propertyName);
		}
		
		DbTable table = getDbTable(tableName);
		List<Column> columns = table.getAllColumns();
		for (Column column: columns) {
			Element propertyDef = createPropertyDefElement(column);
			String propertyName = propertyDef.attributeValue("name");
			if (!propertyNameSet.contains(propertyName)) {
				rootElement.add(propertyDef);
			}
		}
		
		return document;
	}

	protected Element createPropertyDefElement(Column column) {
		String columnName = column.getColumnName();
		String propertyName = column.getPropertyName();
		Assert.notEmpty(propertyName, "propertyName of Column named [" + columnName +"] must not be empty.");
		
		Element propertyDef = DocumentHelper.createElement("PropertyDef");
		propertyDef.addAttribute("name", propertyName);
		JdbcType jdbcType = column.getJdbcType();
		if (jdbcType != null) {
			DataType dataType = jdbcType.getDataType();
			if (dataType != null) {
				Element dataTypeProperty = propertyDef.addElement("Property");
				dataTypeProperty.addAttribute("name", "dataType");
				dataTypeProperty.setText(dataType.getName());
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
