package com.bstek.dorado.jdbc.model.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.data.config.definition.PropertyDefDefinition;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.ModelStrategy;
import com.bstek.dorado.jdbc.config.xml.JdbcParseContext;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;
import com.bstek.dorado.jdbc.model.ColumnDefinition;

public class TableParser extends ObjectParser {
	
	private DataTypeManager dataTypeManager;
	private ModelStrategy modelStrategy;
	
	public DataTypeManager getDataTypeManager() {
		return dataTypeManager;
	}

	public void setDataTypeManager(DataTypeManager dataTypeManager) {
		this.dataTypeManager = dataTypeManager;
	}

	public ModelStrategy getModelStrategy() {
		return modelStrategy;
	}

	public void setModelStrategy(ModelStrategy modelStrategy) {
		this.modelStrategy = modelStrategy;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		TableDefinition tableDef = (TableDefinition)super.doParse(node, context);
		
		JdbcParseContext jdbcContext = (JdbcParseContext) context;
		if (tableDef.isAutoCreateColumns()) {
			this.createColumns(tableDef, jdbcContext);
		}
		if (tableDef.isAutoCreateDataType()) {
			
		}
		return tableDef;
	}
	
	protected void createDataType(TableDefinition tableDef) {
		String dataTypeName = tableDef.getName();
		DataType alreadyType = null;
		try {
			alreadyType = dataTypeManager.getDataType(dataTypeName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if (alreadyType == null) {
			DataTypeDefinitionManager dataTypeDefManager = dataTypeManager.getDataTypeDefinitionManager();
			DataTypeDefinition dataTypeDef = modelStrategy.createDataTypeDefinition(tableDef);
			dataTypeDefManager.registerDefinition(dataTypeName, dataTypeDef);
		} else {
			if (alreadyType instanceof EntityDataType) {
				EntityDataType dataType = (EntityDataType)alreadyType;
				if (dataType.isAutoCreatePropertyDefs()) {
					DataTypeDefinitionManager dataTypeDefManager = dataTypeManager.getDataTypeDefinitionManager();
					DataTypeDefinition alreadyDef = dataTypeDefManager.getDefinition(dataTypeName);
					
					boolean isCache = alreadyDef.isCacheCreatedObject();
					alreadyDef.setCacheCreatedObject(false);
					DataTypeDefinition dataTypeDef = modelStrategy.createDataTypeDefinition(tableDef);
					Map<String, ObjectDefinition> propertyDefMap = dataTypeDef.getPropertyDefs();
					if (propertyDefMap != null && !propertyDefMap.isEmpty()) {
						for (ObjectDefinition objDef: propertyDefMap.values()) {
							PropertyDefDefinition pdef = (PropertyDefDefinition)objDef;
							String propertyName = (String)pdef.getProperty("name");
							
							if (dataTypeDef.getPropertyDef(propertyName) == null) {
								alreadyDef.addPropertyDef(propertyName, pdef);
							}
						}
					}
					
					alreadyDef.setCacheCreatedObject(isCache);
				}
			}
		}
	}
	
	protected void createColumns(TableDefinition tableDef, JdbcParseContext jdbcContext) {
		List<Operation> ops = tableDef.getInitOperations();
		Map<String,ColumnDefinition> columnMap = new HashMap<String,ColumnDefinition>(ops.size());
		for (Operation o: ops) {
			if (o instanceof ColumnDefinition) {
				ColumnDefinition column = (ColumnDefinition)o;
				columnMap.put(column.getName(), column);
			}
		}
		
		
		JdbcEnviroment jdbcEnv = jdbcContext.getJdbcEnviroment();
		
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		String catalog = tableDef.getCatalog();
		String schema = tableDef.getSchema();
		String tableName = tableDef.getTableName();
		
		TableMetaDataGenerator tg = generator.getTableMetaDataGenerator();
		Map<String,String> tableMeta = tg.tableMeta(jdbcEnv, catalog, schema, tableName);
		
		List<Map<String,String>> columnList = tg.listColumnMetas(jdbcEnv, catalog, schema, tableName);
		for (Map<String,String> columnMeta: columnList) {
			String columnName = columnMeta.get(JdbcConstants.COLUMN_NAME);
			ColumnDefinition columnDef = columnMap.get(columnName);
			if (columnDef == null) {
				Map<String,String> columnProperties = tg.columnProperties(columnMeta, jdbcEnv);
				columnDef = tg.createColumnDefinition(tableMeta, columnProperties, jdbcEnv);
				if (columnDef != null) {
					tableDef.addInitOperation(columnDef);
				}
			}
		}
	}

}
