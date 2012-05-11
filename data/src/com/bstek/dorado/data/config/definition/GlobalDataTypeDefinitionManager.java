/**
 * 
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
