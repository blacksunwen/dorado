package com.bstek.dorado.view.config;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.util.Assert;

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
	public void registerDefinition(String name, DataTypeDefinition definition) {
		Assert.notEmpty(name);

		String id = definition.getId();
		if (StringUtils.isEmpty(id)) {
			id = name;
		}
		definition.setId(dataObjectIdPrefix + id);

		super.registerDefinition(name, definition);
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
