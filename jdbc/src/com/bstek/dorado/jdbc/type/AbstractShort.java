package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractShort extends AbstractJdbcType {
	
	public AbstractShort(String jdbcName) {
		super(jdbcName, "Short");
	}

	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		return rs.getShort(columnName);
	}
}
