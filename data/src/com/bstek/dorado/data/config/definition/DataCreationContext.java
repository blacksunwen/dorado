/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

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
