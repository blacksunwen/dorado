package com.bstek.dorado.data.config.definition;

import com.bstek.dorado.data.type.property.BasePropertyDef;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-13
 */
public class PropertyDefDefinition extends GenericObjectDefinition {
	public PropertyDefDefinition() {
		setImplType(BasePropertyDef.class);
	}

	public PropertyDefDefinition(String name) {
		this();
		setName(name);
	}

	public String getName() {
		return (String) getProperty("name");
	}

	public void setName(String name) {
		setProperty("name", name);
	}
}
