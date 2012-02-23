package com.bstek.dorado.jdbc.type;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractBoolean extends AbstractJdbcType {

	public AbstractBoolean(String jdbcName) {
		super(jdbcName, "Boolean");
	}
	
}
