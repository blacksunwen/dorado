package com.bstek.dorado.jdbc.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.type.JdbcType;

public class DeleteCommand {

	private static Log logger = LogFactory.getLog(DeleteCommand.class);
	
	public void execute(TableRecordOperation operation) throws Exception {
		DeleteSql deleteSql = null;
		String sql = null;
		BatchSql batchSql = operation.getBatchSql();
		
		boolean batch = false;
		if (batchSql == null) {
			deleteSql = this.createSql(operation);
		} else {
			deleteSql = batchSql.getDeleteSql();
			if (deleteSql == null) {
				deleteSql = this.createSql(operation);
				batchSql.setDeleteSql(deleteSql);
			} else {
				batch = true;
			}
		}
		sql = deleteSql.getSQL(operation.getDialect());
		
		if (logger.isDebugEnabled()) {
			if (batch) {
				logger.debug("[DELETE-SQL][Batch]" + sql);
			} else {
				logger.debug("[DELETE-SQL]" + sql);
			}
		}
		
		NamedParameterJdbcTemplate jdbcTemplate = operation.getJdbcEnviroment().getSpringNamedDao().getNamedParameterJdbcTemplate();
		JdbcParameterSource parameterSource = this.createParameterSource(operation);
		jdbcTemplate.update(sql, parameterSource);
	}
	
	protected DeleteSql createSql(TableRecordOperation operation) {
		DeleteSql sql = new DeleteSql();
		
		Table table = operation.getDbTable();
		Dialect dialect = operation.getDialect();
		sql.setTableToken(dialect.token(table));
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			String columnName = keyColumn.getName();
			sql.addKeyToken(columnName, propertyName);
		}
		return sql;
	}
	
	protected JdbcParameterSource createParameterSource(TableRecordOperation operation) {
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(null);
		Table table = operation.getDbTable();
		Record record = operation.getRecord();
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			
			Object value = EntityUtils.getOldValue(record, propertyName);
			if (value == null) {
				value = record.get(propertyName);
			}
			
			JdbcType jdbcType = keyColumn.getJdbcType();
			if (jdbcType != null) {
				value = jdbcType.fromDB(value);
			} 
			
			parameterSource.setValue(propertyName, value);
		}
		
		return parameterSource;
	}
	
}
