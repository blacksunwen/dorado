package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractLong extends AbstractJdbcType {

	public AbstractLong(String jdbcName) {
		super(jdbcName, "Long");
	}

	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		return rs.getLong(columnName);
	}

}
