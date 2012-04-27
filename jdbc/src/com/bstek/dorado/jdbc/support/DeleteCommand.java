package com.bstek.dorado.jdbc.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.SqlUtils;

public class DeleteCommand {

	private static Log logger = LogFactory.getLog(DeleteCommand.class);
	
	public void execute(TableRecordOperation operation) throws Exception {
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Dialect dialect = operation.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
		
		DeleteSql deleteSql = this.deleteSql(operation);
		String sql = dialect.toSQL(deleteSql);
		if (logger.isDebugEnabled()) {
			logger.debug("[DELETE-SQL]" + sql);
		}
		JdbcParameterSource parameterSource = deleteSql.getParameterSource();
		jdbcTemplate.update(sql, parameterSource);
	}
	
	private DeleteSql deleteSql(TableRecordOperation operation) {
		Dialect dialect = operation.getDialect();
		
		Table table = operation.getDbTable();
		Record record = operation.getRecord();
		
		DeleteSql sql = new DeleteSql();
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(record);
		sql.setParameterSource(parameterSource);
		sql.setTableToken(dialect.token(table));
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			String columnName = keyColumn.getName();
			sql.addKeyToken(columnName, ":" + propertyName);
		}
		
		return sql;
	}
}
