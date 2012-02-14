package com.bstek.dorado.jdbc.support;

import java.util.List;

import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.PropertyDefDefinition;
import com.bstek.dorado.jdbc.ModelStrategy;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.DbElementCreationContext;
import com.bstek.dorado.jdbc.model.DbElementDefinition;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.type.JdbcType;

public class DefaultModelStrategy implements ModelStrategy {

	@Override
	public DataTypeDefinition createDataTypeDefinition(
			DbElementDefinition dbeDef) {
		DbElementCreationContext context = new DbElementCreationContext();
		DbTable table = null;
		try {
			table = (DbTable)dbeDef.create(context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		String dataTypeName = getDataTypeName(dbeDef);
		DataTypeDefinition dataType = new DataTypeDefinition();
		dataType.setName(dataTypeName);
		dataType.setGlobal(true);
		
		List<Column> columns = table.getAllColumns();
		for (Column column: columns) {
			PropertyDefDefinition propertyDef = new PropertyDefDefinition();
			
			String propertyName = getPropertyName(table, column);
			propertyDef.setProperty("name", propertyName);
			
			JdbcType jdbcType = column.getJdbcType();
			if (jdbcType != null) {
				propertyDef.setProperty("dataType", jdbcType.getDataType());
			}
			dataType.addPropertyDef(propertyName, propertyDef);
		}
		
		return dataType;
	}
	
	protected String getPropertyName(DbTable table, Column column) {
		return column.getKeyName();
	}
	
	protected String getDataTypeName(DbElementDefinition dbeDef) {
		return dbeDef.getName();
	}

	@Override
	public DataProviderDefinition createDataProviderDifinition(
			DbElementDefinition dbeDef) {
		
		return null;
	}

	protected String getDataProviderName(DbElementDefinition dbeDef) {
		return dbeDef.getName();
	}
}
