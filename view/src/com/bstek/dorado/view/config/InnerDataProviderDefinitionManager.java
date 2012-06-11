/**
 * 
 */
package com.bstek.dorado.view.config;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.data.config.definition.DataObjectDefinitionUtils;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-3
 */
public class InnerDataProviderDefinitionManager extends
		DataProviderDefinitionManager {

	private String dataObjectIdPrefix;

	public InnerDataProviderDefinitionManager(
			DefinitionManager<DataProviderDefinition> parent) {
		super(parent);
	}

	public String getDataObjectIdPrefix() {
		return dataObjectIdPrefix;
	}

	public void setDataObjectIdPrefix(String dataObjectIdPrefix) {
		this.dataObjectIdPrefix = dataObjectIdPrefix;
	}

	@Override
	public void registerDefinition(String name,
			DataProviderDefinition definition) {
		Assert.notEmpty(name);

		String id = definition.getId();
		if (StringUtils.isEmpty(id)) {
			id = name;
		}
		DataObjectDefinitionUtils.setDataProviderId(definition,
				dataObjectIdPrefix + id);
		definition.setScope(Scope.request);

		super.registerDefinition(name, definition);
	}

	public InnerDataProviderDefinitionManager duplicate() {
		InnerDataProviderDefinitionManager duplication = new InnerDataProviderDefinitionManager(
				getParent());
		duplication.setDataObjectIdPrefix(dataObjectIdPrefix);
		duplication.getDefinitions().putAll(getDefinitions());
		return duplication;
	}
}