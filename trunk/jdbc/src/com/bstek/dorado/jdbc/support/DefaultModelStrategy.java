package com.bstek.dorado.jdbc.support;

import java.util.List;

import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.PropertyDefDefinition;
import com.bstek.dorado.jdbc.ModelStrategy;
import com.bstek.dorado.jdbc.config.DbElementDefinition;
import com.bstek.dorado.jdbc.config.JdbcCreationContext;
import com.bstek.dorado.jdbc.config.XmlConstants;
import com.bstek.dorado.jdbc.model.AbstractColumn;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.type.JdbcType;

public class DefaultModelStrategy implements ModelStrategy {

	@Override
	public DataTypeDefinition createDataTypeDefinition(
			DbElementDefinition dbeDef) {
		JdbcCreationContext context = new JdbcCreationContext();
		DbTable table = null;
		try {
			table = (DbTable)dbeDef.create(context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		String dataTypeName = this.getDataTypeName(dbeDef);
		DataTypeDefinition dataType = new DataTypeDefinition();
		dataType.setName(dataTypeName);
		dataType.setGlobal(true);
		
		List<AbstractColumn> columns = table.getAllColumns();
		for (AbstractColumn column: columns) {
			PropertyDefDefinition propertyDef = new PropertyDefDefinition();
			
			String propertyName = this.getPropertyName(table, column);
			propertyDef.setProperty("name", propertyName);
			
			JdbcType jdbcType = column.getJdbcType();
			if (jdbcType != null) {
				propertyDef.setProperty("dataType", jdbcType.getDataType());
			}
			dataType.addPropertyDef(propertyName, propertyDef);
		}
		
		return dataType;
	}
	
	@Override
	public DataProviderDefinition createDataProviderDifinition(
			DbElementDefinition dbeDef) {
		DataProviderDefinition providerDef = new DataProviderDefinition();
		String providerName = this.getDataProviderName(dbeDef);
		providerDef.setName(providerName);
		providerDef.setGlobal(true);
		providerDef.setProperty("type", "jdbc");
		providerDef.setProperty(XmlConstants.TABLE_NAME, dbeDef.getName());
		
		return providerDef;
	}
	
	protected String getPropertyName(DbTable table, AbstractColumn column) {
		return column.getPropertyName();
	}
	
	protected String getDataTypeName(DbElementDefinition dbeDef) {
		return dbeDef.getName();
	}

	protected String getDataProviderName(DbElementDefinition dbeDef) {
		return dbeDef.getName();
	}
}
