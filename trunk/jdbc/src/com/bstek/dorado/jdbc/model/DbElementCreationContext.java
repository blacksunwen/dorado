package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.config.definition.CreationContext;

public class DbElementCreationContext extends CreationContext {

	private DbElement dbElement;

	public DbElement getDbElement() {
		return dbElement;
	}

	public void setDbElement(DbElement dbElement) {
		this.dbElement = dbElement;
	}
	
}
