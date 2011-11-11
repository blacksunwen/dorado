package com.bstek.dorado.jdbc.model.table;

import java.util.List;

import com.bstek.dorado.data.entity.EntityEnhancer;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.key.KeyGenerator;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public class TableSqlGenerator implements SqlGenerator {

	public String getType() {
		return "Table";
	}
	
	@Override
	public SelectSql selectSql(JdbcDataProviderOperation operation) {
		Table table = (Table)operation.getDbElement();
		Object parameter = operation.getJdbcContext().getParameter();
		
		//SelectSql
		TableSelectSql selectSql = new TableSelectSql();
				
		//columnsToken
		StringBuilder columnsToken = new StringBuilder();
		List<Column> columns = table.getAllColumns();
		for (int i=0, j=columns.size(), ableColumnCount = 0; i<j; i++) {
			Column column = columns.get(i);
			if (column.isSelectable()) {
				if (ableColumnCount++ > 0) {
					columnsToken.append(',');
				}
				
				String token = column.getColumnName();
				columnsToken.append(token);
			}
		}
		selectSql.setColumnsToken(columnsToken.toString());
		
		//tableToken
		String tableToken = SqlUtils.token(table);
		selectSql.setTableToken(tableToken);
		
		//dynamicToken
		String dynamicToken = table.getDynamicClause();
		dynamicToken = SqlUtils.build(dynamicToken, parameter);
		
		selectSql.setDynamicToken(dynamicToken);
		
		//JdbcParameterSource
		JdbcParameterSource p = SqlUtils.createJdbcParameter(parameter);
		selectSql.setParameterSource(p);
		
		return selectSql;
	}

	@Override
	public InsertSql insertSql(JdbcRecordOperation operation) {
		Table table = (Table)operation.getDbElement();
		Record record = operation.getRecord();
		
		InsertSql sql = new InsertSql();
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(record);
		sql.setParameterSource(parameterSource);
		sql.setTableToken(SqlUtils.token(table));
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			Assert.notEmpty(propertyName, "propertyName of KeyColumn named [" + keyColumn.getColumnName() +"] must not be empty.");
			
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
					
					sql.addColumnToken(keyColumn.getColumnName(), ":"+propertyName);
				}
			} else {
				sql.addColumnToken(keyColumn.getColumnName(), ":"+propertyName);
			}
		}
		
		for (TableColumn column: table.getTableColumns()) {
			if (column.isInsertable()) {
				String propertyName = column.getPropertyName();
				Assert.notEmpty(propertyName, "propertyName of Column named [" + column.getColumnName() +"] must not be empty.");
				
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

	protected void addColumnToken(InsertSql sql,
			JdbcParameterSource parameterSource, TableColumn column,
			String propertyName, Object value) {
		String columnName = column.getColumnName();
		JdbcType jdbcType = column.getJdbcType();
		if (jdbcType != null) {
			Object dbValue = jdbcType.toDB(value);
			parameterSource.setValue(propertyName, dbValue, jdbcType.getSqlType());
		}
		sql.addColumnToken(columnName, ":"+propertyName);
	}

	private boolean hasOldValues(Object entity) {
		EntityEnhancer entityEnhancer = EntityUtils.getEntityEnhancer(entity);
		if (entityEnhancer != null) {
			return entityEnhancer.getOldValues() != null;
		} else {
			return false;
		}
	}
	
	protected String oldPropertyVar(String propertyName) {
		return "_OLD_" + propertyName;
	}
	
	@Override
	public UpdateSql updateSql(JdbcRecordOperation operation) {
		Table table = (Table)operation.getDbElement();
		Record record = operation.getRecord();
		
		UpdateSql sql = new UpdateSql();
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(record);
		sql.setParameterSource(parameterSource);
		sql.setTableToken(SqlUtils.token(table));
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			Assert.notEmpty(propertyName, "propertyName of KeyColumn named [" + keyColumn.getColumnName() +"] must not be empty.");
			
			String columnName = keyColumn.getColumnName();
			Object oldValue = null;
			if (hasOldValues(record) && (oldValue = EntityUtils.getOldValue(record, propertyName)) != null) {
				if (keyColumn.isUpdatable()) {
					sql.addColumnToken(columnName, ":"+propertyName);
					
					String oldPropertyVar = oldPropertyVar(propertyName);
					parameterSource.setValue(oldPropertyVar, oldValue);
					sql.addKeyToken(columnName, ":" + oldPropertyVar);
				} else {
					parameterSource.setValue(propertyName, oldValue);
					sql.addKeyToken(columnName, ":"+propertyName);
				}
			} else {
				sql.addKeyToken(columnName, ":"+propertyName);
			}
		}
		
		for (TableColumn column: table.getTableColumns()) {
			if (column.isUpdatable()) {
				String propertyName = column.getPropertyName();
				Assert.notEmpty(propertyName, "propertyName of Column named [" + column.getColumnName() +"] must not be empty.");
				
				//如果record包含propertyName，那么一定更新到数据库，否则（不包含）如果column具有默认值那么也要更新，否则不更新
				if (record.containsKey(propertyName)) {
					Object value = record.get(propertyName);
					if (value == null) {
						value = column.getUpdateDefaultValue();
					}
					
					this.addColumnToken(sql, parameterSource, column, propertyName, value);
				} else {
					Object value = column.getUpdateDefaultValue();
					if (value != null) {
						this.addColumnToken(sql, parameterSource, column, propertyName, value);
					}
				}
			}
		}
		
		sql.setRetrieveAfterExecute(table.isRetrieveAfterUpdate());
		return sql;
	}

	protected void addColumnToken(UpdateSql sql, JdbcParameterSource parameterSource, 
			TableColumn c, String propertyName, Object value) {
		String columnName = c.getColumnName();
		JdbcType jdbcType = c.getJdbcType();
		if (jdbcType != null) {
			Object dbValue = jdbcType.toDB(value);
			parameterSource.setValue(propertyName, dbValue, jdbcType.getSqlType());
		}
		sql.addColumnToken(columnName, ":"+propertyName);
	}

	@Override
	public DeleteSql deleteSql(JdbcRecordOperation operation) {
		Table table = (Table)operation.getDbElement();
		Record record = operation.getRecord();
		
		DeleteSql sql = new DeleteSql();
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(record);
		sql.setParameterSource(parameterSource);
		sql.setTableToken(SqlUtils.token(table));
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			Assert.notEmpty(propertyName, "propertyName of KeyColumn named [" + keyColumn.getColumnName() +"] must not be empty.");
			
			String columnName = keyColumn.getColumnName();
			sql.addKeyToken(columnName, ":"+propertyName);
		}
		
		return sql;
	}

}
