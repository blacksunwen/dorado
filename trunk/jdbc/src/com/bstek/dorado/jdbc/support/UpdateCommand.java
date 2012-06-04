package com.bstek.dorado.jdbc.support;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.model.table.TableColumn;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.jdbc.type.JdbcType;

public class UpdateCommand {
	
	private static Log logger = LogFactory.getLog(UpdateCommand.class);
	
	private RetrieveCommand retrieveCommand;
	
	public RetrieveCommand getRetrieveCommand() {
		return retrieveCommand;
	}

	public void setRetrieveCommand(RetrieveCommand retrieveCommand) {
		this.retrieveCommand = retrieveCommand;
	}
	
	public void execute(TableRecordOperation operation) throws Exception {
		Record record = operation.getRecord();
		Dialect dialect = operation.getDialect();
		
		UpdateSql updateSql = null;
		String sql = null;
		BatchSql batchSql = operation.getBatchSql();
		
		EntityDataType dataType = null;
		if (batchSql != null) {
			dataType = batchSql.getDataType();
		}
		
		boolean batch = false;
		if (batchSql == null) {
			Set<String> propertyNameSet = (dataType != null)? dataType.getPropertyDefs().keySet() : record.keySet();
			updateSql = this.createSql(operation, propertyNameSet);
			sql = updateSql.getSQL(dialect);
		} else {
			updateSql = batchSql.getUpdateSql();
			if (updateSql == null) {
				Set<String> propertyNameSet = (dataType != null)? dataType.getPropertyDefs().keySet() : record.keySet();
				updateSql = this.createSql(operation, propertyNameSet);
				sql = updateSql.getSQL(dialect);
				batchSql.setUpdateSql(updateSql);
			} else {
				sql = updateSql.getSQL(dialect);
				batch = true;
			}
		}
		
		if (logger.isDebugEnabled()) {
			if (batch) {
				logger.debug("[UPDATE-SQL][Batch]" + sql);
			} else {
				logger.debug("[UPDATE-SQL]" + sql);
			}
		}
		
		JdbcParameterSource parameterSource = this.createParameterSource(updateSql, operation);
		NamedParameterJdbcTemplate jdbcTemplate = operation.getJdbcEnviroment().getSpringNamedDao().getNamedParameterJdbcTemplate();
		jdbcTemplate.update(sql, parameterSource);
		if(updateSql.isRetrieveAfterExecute()) {
			retrieveCommand.execute(operation);
		}
	}
	
	protected UpdateSql createSql(TableRecordOperation operation, Set<String> propertyNameSet) {
		UpdateSql sql = new UpdateSql();
		
		Dialect dialect = operation.getDialect();
		Table table = (Table)operation.getDbTable();
		sql.setTableToken(dialect.token(table));
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			String columnName = keyColumn.getName();
			
			if (keyColumn.isUpdatable()) {
				sql.addColumnToken(columnName, propertyName);
			}
			String keyPropertyVar = keyPropertyVar(propertyName);
			sql.addKeyToken(columnName, keyPropertyVar);
		}
		
		for (String propertyName: propertyNameSet) {
			TableColumn column = table.getTableColumn(propertyName);
			if (column != null && column.isUpdatable()) {
				sql.addColumnToken(propertyName, propertyName);
			}
		}
		
		sql.setRetrieveAfterExecute(table.isRetrieveAfterUpdate());
		return sql;
	}
	
	private String keyPropertyVar(String propertyName) {
		return "_KEY_" + propertyName;
	}
	
	protected JdbcParameterSource createParameterSource(UpdateSql sql, TableRecordOperation operation) {
		Table table = (Table)operation.getDbTable();
		Record record = operation.getRecord();
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(null);
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			String keyPropertyVar = keyPropertyVar(propertyName);
			JdbcType jdbcType = keyColumn.getJdbcType();
			
			Object toDbKeyValue = null;
			Object keyValue = EntityUtils.getOldValue(record, propertyName);
			if (keyValue != null) {
				if (jdbcType != null) {
					toDbKeyValue = jdbcType.toDB(keyValue);
				} else {
					toDbKeyValue = keyValue;
				}
				
				if (keyColumn.isUpdatable()) {
					Object toDbValue = null;
					Object value = record.get(propertyName);
					if (jdbcType != null) {
						toDbValue = jdbcType.toDB(value);
					} else {
						toDbValue = value;
					}
					parameterSource.setValue(propertyName, toDbValue);
				}
			} else {
				Object toDbValue = null;
				Object value = record.get(propertyName);
				if (jdbcType != null) {
					toDbValue = jdbcType.toDB(value);
				} else {
					toDbValue = value;
				}
				if (keyColumn.isUpdatable()) {
					parameterSource.setValue(propertyName, toDbValue);
				}
				
				toDbKeyValue = toDbValue;
			}
			parameterSource.setValue(keyPropertyVar, toDbKeyValue);
		}
		
		String[] columnNames = sql.getColumnNames();
		String[] propertyNames = sql.getPropertyNames();
		for (int i=0; i<columnNames.length; i++) {
			String columnName = columnNames[i];
			String propertyName = propertyNames[i];
			
			AbstractDbColumn dbColumn = table.getColumn(columnName);
			Object value = null;
			if (dbColumn instanceof TableKeyColumn) {
				continue;
			} else {
				value = record.get(propertyName);
				if (value == null) {
					TableColumn column = (TableColumn)dbColumn;
					Object nValue = column.getUpdateDefaultValue();
					
					JdbcType jdbcType = column.getJdbcType();
					if (jdbcType != null) {
						value = jdbcType.fromDB(nValue);
					} else {
						value = nValue;
					}
					
					record.put(propertyName, value);
				}
			}
			
			Object toDbValue = null;
			JdbcType jdbcType = dbColumn.getJdbcType();
			if (jdbcType != null) {
				toDbValue = jdbcType.toDB(value);
			} else {
				toDbValue = value;
			}
			
			parameterSource.setValue(propertyName, toDbValue);
		}
		
		return parameterSource;
	}
}
