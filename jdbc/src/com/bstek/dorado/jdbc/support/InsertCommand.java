package com.bstek.dorado.jdbc.support;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.model.table.KeyGenerator;
import com.bstek.dorado.jdbc.model.table.TableColumn;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.type.JdbcType;

public class InsertCommand {
	
	private static Log logger = LogFactory.getLog(InsertCommand.class);
	
	private RetrieveCommand retrieveCommand;
	
	public RetrieveCommand getRetrieveCommand() {
		return retrieveCommand;
	}

	public void setRetrieveCommand(RetrieveCommand retrieveCommand) {
		this.retrieveCommand = retrieveCommand;
	}

	public void execute(TableRecordOperation operation) throws Exception {
		Record record = operation.getRecord();
		
		InsertSql insertSql = null;
		String sql = null;
		BatchSql batchSql = operation.getBatchSql();
		
		EntityDataType dataType = null;
		if (batchSql != null) {
			dataType = batchSql.getDataType();
		}
		
		boolean batch = false;
		if (batchSql == null) {
			Set<String> propertyNameSet = (dataType != null)? dataType.getPropertyDefs().keySet() : record.keySet();
			insertSql = this.createSql(operation, propertyNameSet);
		} else {
			insertSql = batchSql.getInsertSql();
			if (insertSql == null) {
				Set<String> propertyNameSet = (dataType != null)? dataType.getPropertyDefs().keySet() : record.keySet();
				insertSql = this.createSql(operation, propertyNameSet);
				batchSql.setInsertSql(insertSql);
			} else {
				batch = true;
			}
		}
		sql = insertSql.getSQL(operation.getDialect());
		
		if (logger.isDebugEnabled()) {
			if (batch) {
				logger.debug("[INSERT-SQL][Batch]" + sql);
			} else {
				logger.debug("[INSERT-SQL]" + sql);
			}
		}
		
		JdbcParameterSource parameterSource = this.createParameterSource(operation, insertSql);
		NamedParameterJdbcTemplate jdbcTemplate = operation.getJdbcEnviroment().getSpringNamedDao().getNamedParameterJdbcTemplate();
		TableKeyColumn identityColumn = insertSql.getIdentityColumn();
		if (identityColumn == null) {
			jdbcTemplate.update(sql, parameterSource);
		} else {
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(sql, parameterSource, keyHolder);
			String propertyName = identityColumn.getPropertyName();
			if (StringUtils.isNotEmpty(propertyName)) {
				Object value = keyHolder.getKey();
				JdbcType jdbcType = identityColumn.getJdbcType();
				if (jdbcType != null) {
					value = jdbcType.fromDB(value);
				}
				
				record.put(propertyName, value);
			}
		}
		
		if(insertSql.isRetrieveAfterExecute()) {
			retrieveCommand.execute(operation);
		}
	}
	
	protected InsertSql createSql(TableRecordOperation operation, Set<String> propertyNameSet) {
		InsertSql sql = new InsertSql();
		
		Dialect dialect = operation.getDialect();
		Table table = (Table)operation.getDbTable();
		sql.setTableToken(dialect.token(table));
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			KeyGenerator<?> keyGenerator = keyColumn.getKeyGenerator();
			if (keyGenerator != null && keyGenerator.isIdentity()) {
				sql.setIdentityColumn(keyColumn);
			} else {
				sql.addColumnToken(keyColumn.getName(), propertyName);
			}
		}
		
		for (String propertyName: propertyNameSet) {
			TableColumn column = table.getTableColumn(propertyName);
			if (column != null && column.isInsertable()) {
				sql.addColumnToken(propertyName, propertyName);
			}
		}
		
		sql.setRetrieveAfterExecute(table.isRetrieveAfterInsert());
		
		return sql;
	}
	
	protected JdbcParameterSource createParameterSource(TableRecordOperation operation, InsertSql sql) {
		Table table = (Table)operation.getDbTable();
		Record record = operation.getRecord();
		
		String[] columnNames = sql.getColumnNames();
		String[] propertyNames = sql.getPropertyNames();
		
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(null);
		for (int i=0; i<columnNames.length; i++) {
			String columnName = columnNames[i];
			String propertyName = propertyNames[i];
			
			AbstractDbColumn dbColumn = table.getColumn(columnName);
			Object value = null;
			if (dbColumn instanceof TableKeyColumn) {
				TableKeyColumn keyColumn = (TableKeyColumn)dbColumn;
				KeyGenerator<?> keyGenerator = keyColumn.getKeyGenerator();
				if (keyGenerator != null) {
					Object nValue = keyGenerator.newKey(operation, keyColumn);
					
					JdbcType jdbcType = keyColumn.getJdbcType();
					if (jdbcType != null) {
						value = jdbcType.fromDB(nValue);
					} else {
						value = nValue;
					}
					
					record.put(propertyName, value);
				} else {
					value = record.get(propertyName);
				}
			} else {
				value = record.get(propertyName);
				if (value == null) {
					TableColumn column = (TableColumn)dbColumn;
					Object nValue = column.getInsertDefaultValue();
					
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
