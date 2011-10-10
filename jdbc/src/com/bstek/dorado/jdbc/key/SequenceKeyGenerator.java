package com.bstek.dorado.jdbc.key;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcQueryContext;
import com.bstek.dorado.jdbc.model.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;

public class SequenceKeyGenerator implements KeyGenerator<Number> {

	public String getName() {
		return "SEQUENCE";
	}

	public boolean isIdentity() {
		return false;
	}

	@Override
	public Number newKey(JdbcQueryContext context, TableKeyColumn keyColumn) {
		String sequenceName = (String) keyColumn.getKeyParameter();

		JdbcEnviroment jdbcEnviroment = context.getJdbcEnviroment();
		Dialect dialect = jdbcEnviroment.getDialect();
		if (!dialect.isSequenceSupport()) {
			throw new UnsupportedOperationException();
		}

		String sql = dialect.sequenceSql(sequenceName);
		
		JdbcTemplate template = jdbcEnviroment.getNamedDao().getJdbcTemplate();

		Number value = template.query(sql, new ResultSetExtractor<Number>() {
			public Number extractData(ResultSet resultSet)
					throws SQLException, DataAccessException {
				resultSet.next();
				return (Number) resultSet.getObject(1);
			}
		});
		
		return value;
	}

}
