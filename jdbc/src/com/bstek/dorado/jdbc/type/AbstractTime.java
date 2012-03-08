package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AbstractTime extends AbstractJdbcType {

	public AbstractTime(String jdbcName) {
		super(jdbcName, "Time");
	}

	@Override
	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		return rs.getTime(columnName);
	}

}
