package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractFloat extends AbstractJdbcType {

	public AbstractFloat(String jdbcName) {
		super(jdbcName, "Float");
	}

	@Override
	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		return rs.getFloat(columnName);
	}

}
