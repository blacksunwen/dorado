package com.bstek.dorado.jdbc.model.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.util.Assert;

/**
 * 数据库序列的主键生成器
 * 
 * @author mark
 * 
 */
public class SequenceKeyGenerator extends AbstractKeyGenerator<Number> {

	public SequenceKeyGenerator() {
		super();
		this.setName("SEQUENCE");
	}

	@Override
	public Number newKey(JdbcRecordOperation operation, TableKeyColumn keyColumn) {
		String sequenceName = (String) keyColumn.getKeyParameter();
		Assert.notEmpty(sequenceName, "sequenceName must not be empty.");
		
		JdbcEnviroment jdbcEnviroment = operation.getJdbcEnviroment();
		Dialect dialect = jdbcEnviroment.getDialect();
		if (!dialect.isSequenceSupport()) {
			throw new UnsupportedOperationException("[" + jdbcEnviroment.getName() + "] does not support db sequence.");
		}

		String sql = dialect.sequenceSql(sequenceName);

		JdbcTemplate template = jdbcEnviroment.getNamedDao().getJdbcTemplate();

		Number value = template.query(sql, new ResultSetExtractor<Number>() {
			public Number extractData(ResultSet resultSet) throws SQLException,
					DataAccessException {
				resultSet.next();
				return (Number) resultSet.getObject(1);
			}
		});

		return value;
	}

}
