package com.bstek.dorado.jdbc.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractBigDecimal extends AbstractJdbcType {

	public AbstractBigDecimal(String jdbcName) {
		super(jdbcName, "BigDecimal");
	}

	@Override
	public Object fromDB(ResultSet rs, String columnName) throws SQLException {
		return rs.getBigDecimal(columnName);
	}

}
