package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractInteger extends AbstractJdbcType {

	public AbstractInteger(String jdbcName) {
		super(jdbcName, "Integer");
	}

	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		return rs.getInt(columnName);
	}

}
