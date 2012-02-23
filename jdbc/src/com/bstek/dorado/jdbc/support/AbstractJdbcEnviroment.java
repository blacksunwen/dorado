package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractJdbcEnviroment implements JdbcEnviroment {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private boolean isdefault = false;

	public boolean isDefault() {
		return isdefault;
	}

	public void setDefault(boolean isdefault) {
		this.isdefault = isdefault;
	}

	private Dialect dialect;

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

}
