package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractDate extends AbstractJdbcType {

	public AbstractDate(String jdbcName) {
		super(jdbcName, "Date");
	}

	@Override
	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		return rs.getDate(columnName);
	}

}
