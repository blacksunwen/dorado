/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
/**
 * 
 */
package com.bstek.dorado.view.config;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.data.config.definition.DataObjectDefinitionUtils;
import com.bstek.dorado.data.config.definition.DataResolverDefinition;
import com.bstek.dorado.data.config.definition.DataResolverDefinitionManager;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-3
 */
public class InnerDataResolverDefinitionManager extends
		DataResolverDefinitionManager {
	private String dataObjectIdPrefix;
	private ViewConfigDefinition viewConfigDefinition;

	public InnerDataResolverDefinitionManager(
			DefinitionManager<DataResolverDefinition> parent) {
		super(parent);
	}

	public String getDataObjectIdPrefix() {
		return dataObjectIdPrefix;
	}

	public void setDataObjectIdPrefix(String dataObjectIdPrefix) {
		this.dataObjectIdPrefix = dataObjectIdPrefix;
	}

	public void setViewConfigDefinition(
			ViewConfigDefinition viewConfigDefinition) {
		this.viewConfigDefinition = viewConfigDefinition;
	}

	public ViewConfigDefinition getViewConfigDefinition() {
		return viewConfigDefinition;
	}

	@Override
	public void registerDefinition(String name,
			DataResolverDefinition definition) {
		Assert.notEmpty(name);

		String id = definition.getId();
		if (StringUtils.isEmpty(id)) {
			id = name;
		}
		DataObjectDefinitionUtils.setDataResolverId(definition,
				dataObjectIdPrefix + id);
		definition.setScope(Scope.request);

		super.registerDefinition(name, definition);
	}

	public InnerDataResolverDefinitionManager duplicate() {
		InnerDataResolverDefinitionManager duplication = new InnerDataResolverDefinitionManager(
				getParent());
		duplication.setDataObjectIdPrefix(dataObjectIdPrefix);
		duplication.setViewConfigDefinition(viewConfigDefinition);
		duplication.getDefinitions().putAll(getDefinitions());
		return duplication;
	}
}
