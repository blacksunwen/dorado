package com.bstek.dorado.jdbc.support;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.model.table.TableColumn;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.RetrieveSql;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public class RetrieveCommand {

	private static Log logger = LogFactory.getLog(RetrieveCommand.class);
	
	public void execute(TableRecordOperation operation) throws Exception {
		Record record = operation.getRecord();
		
		RetrieveSql retrieveSql = null;
		String sql = null;
		BatchSql batchSql = operation.getBatchSql();
		
		EntityDataType dataType = null;
		if (batchSql != null) {
			dataType = batchSql.getDataType();
		}
		
		boolean batch = false;
		if (batchSql == null) {
			Set<String> propertyNameSet = (dataType != null)? dataType.getPropertyDefs().keySet() : record.keySet();
			retrieveSql = this.createSql(operation, propertyNameSet);
		} else {
			retrieveSql = batchSql.getRetrieveSql();
			if (retrieveSql == null) {
				Set<String> propertyNameSet = (dataType != null)? dataType.getPropertyDefs().keySet() : record.keySet();
				retrieveSql = this.createSql(operation, propertyNameSet);
				batchSql.setRetrieveSql(retrieveSql);
			} else {
				batch = true;
			}
		}
		sql = retrieveSql.getSQL(operation.getDialect());
		
		if (logger.isDebugEnabled()) {
			if (batch) {
				logger.debug("[RETRIEVE-SQL][Batch]" + sql);
			} else {
				logger.debug("[RETRIEVE-SQL]" + sql);
			}
		}
		
		JdbcParameterSource parameterSource = this.createParameterSource(operation, retrieveSql);
		NamedParameterJdbcTemplate jdbcTemplate = operation.getJdbcEnviroment().getSpringNamedDao().getNamedParameterJdbcTemplate();
		List<Record> rs = jdbcTemplate.query(sql, parameterSource, retrieveSql.getRecordRowMapper());
		Assert.isTrue(rs.size() == 1, "[" + rs.size() +"] record(s) retrieved, only 1 excepted.");
		
		Record rRecord = rs.get(0);
		record.putAll(rRecord);
	}
	
	protected RetrieveSql createSql(TableRecordOperation operation, Set<String> propertyNameSet) {
		Dialect dialect = operation.getDialect();
		Table table = (Table)operation.getDbTable();
		RetrieveSql sql = new RetrieveSql(table);
		sql.setTableToken(dialect.token(table));
		
		for (String propertyName: propertyNameSet) {
			TableColumn column = table.getTableColumn(propertyName);
			if (column != null && column.isSelectable()) {
				sql.addColumnToken(column);
			}
		}
		
		return sql;
	}
	
	protected JdbcParameterSource createParameterSource(TableRecordOperation operation, RetrieveSql retrieveSql) {
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(null);
		Record record = operation.getRecord();
		Table table = (Table)operation.getDbTable();
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			Object value = record.get(propertyName);
			
			JdbcType jdbcType = keyColumn.getJdbcType();
			if (jdbcType != null) {
				value = jdbcType.fromDB(value);
			}
			
			parameterSource.setValue(propertyName, value);
		}
		
		return parameterSource;
	}
}
