package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.jdbc.model.DbElementDefinition;

public interface ModelStrategy {

	DataTypeDefinition createDataTypeDefinition(DbElementDefinition dbeDef);
	
	DataProviderDefinition createDataProviderDifinition(DbElementDefinition dbeDef);
}
