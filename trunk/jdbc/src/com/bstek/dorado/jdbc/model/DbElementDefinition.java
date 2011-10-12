package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.config.definition.ObjectDefinition;

public class DbElementDefinition extends ObjectDefinition {

	public String getName() {
		return (String)this.getProperties().get("name");
	}
}
