package com.bstek.dorado.jdbc.config;

import java.util.Map;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.data.config.definition.PropertyDefDefinition;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.jdbc.ModelStrategy;

public abstract class AbstractDbTableParser extends ObjectParser {
	private ModelStrategy modelStrategy;
	private DataTypeManager dataTypeManager;
	private DataProviderManager dataProviderManager;
	
	public void setModelStrategy(ModelStrategy modelStrategy) {
		this.modelStrategy = modelStrategy;
	}

	public void setDataTypeManager(DataTypeManager dataTypeManager) {
		this.dataTypeManager = dataTypeManager;
	}

	public void setDataProviderManager(DataProviderManager dataProviderManager) {
		this.dataProviderManager = dataProviderManager;
	}
	
	public ModelStrategy getModelStrategy() {
		return modelStrategy;
	}

	public DataTypeManager getDataTypeManager() {
		return dataTypeManager;
	}

	public DataProviderManager getDataProviderManager() {
		return dataProviderManager;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		AbstractDbTableDefinition tableDef = (AbstractDbTableDefinition)super.doParse(node, context);
		JdbcParseContext jdbcContext = (JdbcParseContext) context;
		
		this.doAutoCreate(tableDef, jdbcContext);
		
		return tableDef;
	}
	
	protected void doAutoCreate(AbstractDbTableDefinition tableDef, JdbcParseContext jdbcContext) throws Exception {
		if (tableDef.isAutoCreateDataType()) {
			this.createDataType(tableDef);
		}
		if (tableDef.isAutoCreateDataProvider()) {
			this.createDataProvider(tableDef);
		}
	}
	
	protected void createDataProvider(AbstractDbTableDefinition tableDef) throws Exception {
		String providerName = tableDef.getName();
		if (dataProviderManager.getDataProvider(providerName) == null) {
			DataProviderDefinitionManager defManager = dataProviderManager.getDataProviderDefinitionManager();
			DataProviderDefinition def = modelStrategy.createDataProviderDifinition(tableDef);
			if (def != null) {
				defManager.registerDefinition(def.getName(), def);
			}
		} else {
			throw new Exception("DataProvider named [" + providerName + "] already exists.");
		}
	}
	
	protected void createDataType(AbstractDbTableDefinition tableDef) throws Exception {
		String dataTypeName = tableDef.getName();
		DataType alreadyType = null;
		alreadyType = dataTypeManager.getDataType(dataTypeName);
		
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
							
							if (alreadyDef.getPropertyDef(propertyName) == null) {
								alreadyDef.addPropertyDef(propertyName, pdef);
							}
						}
					}
					
					alreadyDef.setCacheCreatedObject(isCache);
				}
			}
		}
	}

}
