package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractByte extends AbstractJdbcType {

	public AbstractByte(String jdbcName) {
		super(jdbcName, "Byte");
	}

	@Override
	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		return rs.getByte(columnName);
	}

}
