package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public abstract class AbstractDateTime extends AbstractJdbcType {

	public AbstractDateTime(String jdbcName) {
		super(jdbcName, "DateTime");
	}

	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		Timestamp value = rs.getTimestamp(columnName);
		return fromDB(value);
	}

}
