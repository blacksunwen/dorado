package com.bstek.dorado.jdbc.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;
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
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Record record = operation.getRecord();
		
		InsertSql insertSql = this.insertSql(operation);
		Dialect dialect = operation.getDialect();
		String sql = dialect.toSQL(insertSql);
		if (logger.isDebugEnabled()) {
			logger.debug("[INSERT-SQL]" + sql);
		}
		
		NamedParameterJdbcTemplate jdbcTemplate = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
		TableKeyColumn identityColumn = insertSql.getIdentityColumn();
		if (identityColumn == null) {
			JdbcParameterSource parameterSource = insertSql.getParameterSource();
			jdbcTemplate.update(sql, parameterSource);
		} else {
			JdbcParameterSource parameterSource = insertSql.getParameterSource();
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
	
	private InsertSql insertSql(TableRecordOperation operation) {
		Dialect dialect = operation.getDialect();
		
		Table table = (Table)operation.getDbTable();
		Record record = operation.getRecord();
		
		InsertSql sql = new InsertSql();
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(record);
		sql.setParameterSource(parameterSource);
		sql.setTableToken(dialect.token(table));
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			KeyGenerator<?> keyGenerator = keyColumn.getKeyGenerator();
			if (keyGenerator != null) {
				if (keyGenerator.isIdentity()) {
					sql.setIdentityColumn(keyColumn);
				} else {
					Object value = keyGenerator.newKey(operation, keyColumn);
					JdbcType jdbcType = keyColumn.getJdbcType();
					if (jdbcType != null) {
						record.put(propertyName, jdbcType.fromDB(value));
						parameterSource.setValue(propertyName, jdbcType.toDB(value), jdbcType.getSqlType());
					} else {
						record.put(propertyName, value);
					}
					
					sql.addColumnToken(keyColumn.getName(), ":"+propertyName);
				}
			} else {
				sql.addColumnToken(keyColumn.getName(), ":"+propertyName);
			}
		}
		
		for (TableColumn column: table.getTableColumns()) {
			if (column.isInsertable()) {
				String propertyName = column.getPropertyName();
				if (record.containsKey(propertyName)) {
					Object value = record.get(propertyName);
					if (value == null) {
						value = column.getInsertDefaultValue();
						if (value != null) {
							JdbcType jdbcType = column.getJdbcType();
							if (jdbcType != null) {
								value = jdbcType.fromDB(value);
								record.set(propertyName, value);
							} else {
								record.set(propertyName, value);
							}
						}
					}
					
					addColumnToken(sql, parameterSource, column, propertyName,
							value);
				} else {
					Object value = column.getInsertDefaultValue();
					if (value != null) {
						addColumnToken(sql, parameterSource, column,
								propertyName, value);
					}
				}
			}
		}
		
		sql.setRetrieveAfterExecute(table.isRetrieveAfterInsert());
		return sql;
	}
	
	private void addColumnToken(InsertSql sql,
			JdbcParameterSource parameterSource, TableColumn column,
			String propertyName, Object value) {
		String columnName = column.getName();
		JdbcType jdbcType = column.getJdbcType();
		if (jdbcType != null) {
			Object dbValue = jdbcType.toDB(value);
			parameterSource.setValue(propertyName, dbValue, jdbcType.getSqlType());
		}
		sql.addColumnToken(columnName, ":" + propertyName);
	}
	
}
