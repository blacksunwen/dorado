package com.bstek.dorado.data.config.definition;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.DefinitionReference;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-14
 */
public class DataCreationContext extends CreationContext {
	private DefinitionReference<DataTypeDefinition> dataTypeDefinition;

	public DefinitionReference<DataTypeDefinition> getCurrentDataTypeDefinition() {
		return dataTypeDefinition;
	}

	public void setCurrentDataTypeDefinition(
			DefinitionReference<DataTypeDefinition> dataTypeDefinition) {
		this.dataTypeDefinition = dataTypeDefinition;
	}
}
