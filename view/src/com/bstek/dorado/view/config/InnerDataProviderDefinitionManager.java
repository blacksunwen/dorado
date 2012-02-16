/**
 * 
 */
package com.bstek.dorado.view.config;

import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-3
 */
public class InnerDataProviderDefinitionManager extends
		DataProviderDefinitionManager {
	public InnerDataProviderDefinitionManager(
			DefinitionManager<DataProviderDefinition> parent) {
		super(parent);
	}
}
