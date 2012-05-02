package com.bstek.dorado.jdbc.support;

import java.util.List;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.data.config.definition.PropertyDefDefinition;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.type.DefaultEntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.ModelStrategy;
import com.bstek.dorado.jdbc.config.AbstractDbTableDefinition;
import com.bstek.dorado.jdbc.config.XmlConstants;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.type.JdbcType;

/**
 * 默认的JDBC模型与dorado模型转换器
 * 
 * @author mark.li@bstek.com
 * 
 */
public class DefaultModelStrategy implements ModelStrategy {

	private DataTypeManager dataTypeManager;
	private DataProviderManager dataProviderManager;

	public void setDataTypeManager(DataTypeManager dataTypeManager) {
		this.dataTypeManager = dataTypeManager;
	}

	public DataTypeManager getDataTypeManager() {
		return dataTypeManager;
	}

	public DataProviderManager getDataProviderManager() {
		return dataProviderManager;
	}

	public void setDataProviderManager(DataProviderManager dataProviderManager) {
		this.dataProviderManager = dataProviderManager;
	}

	public DataTypeDefinition createDataType(AbstractDbTableDefinition tableDef)
			throws Exception {
		String name = tableDef.getName();
		DataTypeDefinitionManager manager = dataTypeManager
				.getDataTypeDefinitionManager();
		DataTypeDefinition def = manager.getDefinition(name);
		if (def == null) {
			def = this.createDataTypeDefinition(tableDef);
			def.setImpl(DefaultEntityDataType.class.getName());
			def.setName(name);
			def.setGlobal(true);
			manager.registerDefinition(name, def);
			
			return def;
		}
		
		return null;
	}

	public DataProviderDefinition createDataProvider(AbstractDbTableDefinition tableDef)
			throws Exception {
		String name = tableDef.getName();
		DataProviderDefinitionManager manager = dataProviderManager
				.getDataProviderDefinitionManager();
		DataProviderDefinition def = manager.getDefinition(name);
		if (def == null) {
			def = createDataProviderDifinition(tableDef);
			def.setName(name);
			def.setGlobal(true);
			def.setProperty(XmlConstants.TABLE_NAME, name);
			def.setImpl(JdbcDataProvider.class.getName());
			manager.registerDefinition(name, def);

			return def;
		}
		
		return null;
	}

	protected DataTypeDefinition createDataTypeDefinition(
			AbstractDbTableDefinition tableDef) {
		CreationContext context = new CreationContext();
		DbTable table = null;
		try {
			table = (DbTable) tableDef.create(context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		DataTypeDefinition dataType = new DataTypeDefinition();
		List<AbstractDbColumn> columns = table.getAllColumns();
		for (AbstractDbColumn column : columns) {
			PropertyDefDefinition propertyDef = new PropertyDefDefinition();
			propertyDef.setImpl(BasePropertyDef.class.getName());
			String propertyName = column.getPropertyName();
			propertyDef.setProperty("name", propertyName);

			JdbcType jdbcType = column.getJdbcType();
			if (jdbcType != null) {
				propertyDef.setProperty("dataType", jdbcType.getDataType());
			}
			dataType.addPropertyDef(propertyName, propertyDef);
		}

		return dataType;
	}

	protected DataProviderDefinition createDataProviderDifinition(
			AbstractDbTableDefinition tableDef) {
		DataProviderDefinition def = new DataProviderDefinition();
		return def;
	}

}
