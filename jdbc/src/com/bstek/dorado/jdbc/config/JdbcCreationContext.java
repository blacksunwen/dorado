package com.bstek.dorado.jdbc.config;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.jdbc.model.DbElement;

public class JdbcCreationContext extends CreationContext {

	private DbElement dbElement;

	public DbElement getDbElement() {
		return dbElement;
	}

	public void setDbElement(DbElement dbElement) {
		this.dbElement = dbElement;
	}
	
}
