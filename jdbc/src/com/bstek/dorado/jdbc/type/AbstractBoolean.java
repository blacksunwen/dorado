package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractBoolean extends AbstractJdbcType {

	public AbstractBoolean(String jdbcName) {
		super(jdbcName, "Boolean");
	}

	@Override
	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		return rs.getBoolean(columnName);
	}
	
}
