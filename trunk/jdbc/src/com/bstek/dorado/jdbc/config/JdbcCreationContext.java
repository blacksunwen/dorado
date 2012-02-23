package com.bstek.dorado.jdbc.config;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.jdbc.model.DbElement;

/**
 * JDBC模块定义对象实例化时候的上下文对象
 * 
 * @author mark.li@bstek.com
 *
 */
public class JdbcCreationContext extends CreationContext {

	private DbElement dbElement;

	public DbElement getDbElement() {
		return dbElement;
	}

	public void setDbElement(DbElement dbElement) {
		this.dbElement = dbElement;
	}
	
}
