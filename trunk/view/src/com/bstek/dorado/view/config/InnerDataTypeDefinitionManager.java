package com.bstek.dorado.view.config;

import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.data.config.DataTypeName;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-28
 */
public class InnerDataTypeDefinitionManager extends DataTypeDefinitionManager {
	private static final String GLOBAL_PREFIX = "global:";

	private String dataObjectIdPrefix;

	public InnerDataTypeDefinitionManager(
			DefinitionManager<DataTypeDefinition> parent) {
		super(parent);
	}

	public String getDataObjectIdPrefix() {
		return dataObjectIdPrefix;
	}

	public void setDataObjectIdPrefix(String dataObjectIdPrefix) {
		this.dataObjectIdPrefix = dataObjectIdPrefix;
	}

	@Override
	protected DataTypeDefinition createAggregationDataType(
			DataTypeName dataTypeName) {
		DataTypeDefinition definition = super
				.createAggregationDataType(dataTypeName);
		definition.setId(dataObjectIdPrefix + definition.getId());
		return definition;
	}

	@Override
	public DataTypeDefinition getDefinition(String name) {
		DataTypeDefinition definition = null;
		if (name.startsWith(GLOBAL_PREFIX)) {
			DefinitionManager<DataTypeDefinition> parent = getParent();
			if (parent != null) {
				definition = parent.getDefinition(name.substring(GLOBAL_PREFIX
						.length()));
			}
		} else {
			definition = super.getDefinition(name);
		}
		return definition;
	}
}
