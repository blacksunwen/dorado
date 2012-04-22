package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractDouble extends AbstractJdbcType {

	public AbstractDouble(String jdbcName) {
		super(jdbcName, "Double");
	}

	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		return rs.getDouble(columnName);
	}

}
