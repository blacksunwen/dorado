package com.bstek.dorado.jdbc.model.storedprogram;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.SqlReturnType;

import com.bstek.dorado.jdbc.type.JdbcType;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class JdbcReturnType implements SqlReturnType {

	private JdbcType jdbcType;
	
	public JdbcReturnType(JdbcType jdbcType) {
		this.jdbcType = jdbcType;
	}
	
	public Object getTypeValue(CallableStatement cs, int paramIndex,
			int sqlType, String typeName) throws SQLException {
		Object dbValue = cs.getObject(paramIndex);
		return jdbcType.fromDB(dbValue);
	}

}
