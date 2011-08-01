/**
 * 
 */
package com.bstek.dorado.data.config.definition;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.data.type.property.PropertyDef;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-26
 */
public class SelfDataTypeDefinition extends DataTypeDefinition {
	public static final SelfDataTypeDefinition INSTANCE = new SelfDataTypeDefinition(
			PropertyDef.SELF_DATA_TYPE_NAME);

	private SelfDataTypeDefinition(String name) {
		setName(name);
	}

	@Override
	protected Object doCreate(CreationContext context) throws Exception {
		throw new UnsupportedOperationException();
	}
}
