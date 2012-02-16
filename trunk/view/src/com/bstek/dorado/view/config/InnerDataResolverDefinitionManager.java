/**
 * 
 */
package com.bstek.dorado.view.config;

import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.data.config.definition.DataResolverDefinition;
import com.bstek.dorado.data.config.definition.DataResolverDefinitionManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-3
 */
public class InnerDataResolverDefinitionManager extends
		DataResolverDefinitionManager {
	public InnerDataResolverDefinitionManager(
			DefinitionManager<DataResolverDefinition> parent) {
		super(parent);
	}
}
