package com.bstek.dorado.jdbc.support;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.DeleteAllSql;
import com.bstek.dorado.jdbc.sql.SqlUtils;

public class DeleteAllCommand {

	private static Log logger = LogFactory.getLog(DeleteAllCommand.class);
	
	private boolean allowEmptyCondition = false;
	
	public boolean isAllowEmptyCondition() {
		return allowEmptyCondition;
	}

	public void setAllowEmptyCondition(boolean allowEmptyCondition) {
		this.allowEmptyCondition = allowEmptyCondition;
	}

	public void execute(DeleteAllOperation operation) throws Exception {
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
		
		DeleteAllSql deleteSql = this.deleteAllSql(operation);
		String sql = dialect.toSQL(deleteSql);
		if (logger.isDebugEnabled()) {
			logger.debug("[DELETE-ALL-SQL]" + sql);
		}
		
		JdbcParameterSource parameterSource = deleteSql.getParameterSource();
		jdbcTemplate.update(sql, parameterSource);
	}
	
	private DeleteAllSql deleteAllSql(DeleteAllOperation operation) {
		Dialect dialect = operation.getJdbcEnviroment().getDialect();
		Table table = operation.getDbTable();
		
		DeleteAllSql sql = new DeleteAllSql();
		Map<String, Object> columnValueMap = operation.getColumnValueMap();
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(columnValueMap);
		sql.setParameterSource(parameterSource);
		sql.setTableToken(dialect.token(table));
		
		if (columnValueMap.isEmpty() && !isAllowEmptyCondition()) {
			throw new IllegalArgumentException("delete condition could not be empty. [" + table.getName() + "]");
		}
		
		for (String columnName: columnValueMap.keySet()) {
			AbstractDbColumn column = table.getColumn(columnName);
			String propertyName = column.getPropertyName();
			
			sql.addColumnToken(columnName, ":" + propertyName);
		}
		
		return sql;
	}
}
