package com.bstek.dorado.jdbc.support;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.type.JdbcType;

public class DefaultModelGenerator extends AbstractModelGenerator {

	@Override
	protected String columnName(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv) {
		return column.get(JdbcConstants.COLUMN_NAME);
	}
	
	@Override
	protected String propertyName(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv) {
		return columnName(column, table, jdbcEnv);
	}
	
	@Override
	protected String keyGenerator(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv) {
		if (JdbcConstants.YES.equalsIgnoreCase(column.get(JdbcConstants.IS_AUTOINCREMENT))) {
			return JdbcConstants.IDENTITY;
		} else {
			return null;
		}
	}
	
	@Override
	protected String jdbcType(Map<String,String> column, Map<String,String> table, JdbcEnviroment jdbcEnv) {
		String dataType = column.get(JdbcConstants.DATA_TYPE);
		if (StringUtils.isNotEmpty(dataType)) {
			int code = Integer.valueOf(dataType);
			Dialect dialect = jdbcEnv.getDialect();
			List<JdbcType> jdbcTypes = dialect.getJdbcTypes();
			for (JdbcType jdbcType: jdbcTypes) {
				if (code == jdbcType.getJdbcCode()) {
					return jdbcType.getName();
				}
			}
		}
		
		return null;
	}
}
