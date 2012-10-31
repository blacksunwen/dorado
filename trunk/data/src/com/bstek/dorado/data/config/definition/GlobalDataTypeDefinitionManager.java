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
package com.bstek.dorado.data.config.definition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-10
 */
public class GlobalDataTypeDefinitionManager extends DataTypeDefinitionManager {
	public GlobalDataTypeDefinitionManager() {
		super();
	}

	@Override
	public void registerDefinition(String name, DataTypeDefinition definition) {
		DataObjectDefinitionUtils.setDataTypeGlobal(definition, true);
		super.registerDefinition(name, definition);
	}

	@Override
	public DataTypeDefinition unregisterDefinition(String name) {
		DataTypeDefinition definition = super.unregisterDefinition(name);
		if (definition != null) {
			DataObjectDefinitionUtils.setDataTypeGlobal(definition, false);
		}
		return definition;
	}

}
