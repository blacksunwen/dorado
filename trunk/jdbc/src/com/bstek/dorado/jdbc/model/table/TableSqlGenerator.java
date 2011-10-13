package com.bstek.dorado.jdbc.model.table;

import java.util.List;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcDataProviderContext;
import com.bstek.dorado.jdbc.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.key.KeyGenerator;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public class TableSqlGenerator implements SqlGenerator<Table> {

	public DbElement.Type getType() {
		return DbElement.Type.Table;
	}
	
	@Override
	public SelectSql selectSql(Table table, Object parameter,
			JdbcDataProviderContext jdbcContext) {
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
		
		//fromToken
		String fromToken = SqlUtils.token(table);
		selectSql.setFromToken(fromToken);
		
		//dynamicToken
		String dynamicToken = table.getDynamicToken();
		dynamicToken = SqlUtils.build(dynamicToken, parameter);
		
		selectSql.setDynamicToken(dynamicToken);
		
		//JdbcParameterSource
		JdbcParameterSource p = SqlUtils.createJdbcParameter(parameter);
		selectSql.setParameterSource(p);
		
		return selectSql;
	}

	@Override
	public InsertSql insertSql(Table table, Record record, JdbcDataResolverContext jdbcContext) {
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
					Object value = keyGenerator.newKey(jdbcContext, keyColumn, record);
					JdbcType jdbcType = keyColumn.getJdbcType();
					if (jdbcType != null) {
						record.put(propertyName, jdbcType.fromDB(value));
						parameterSource.setValue(propertyName, jdbcType.toDB(value), jdbcType.getJdbcCode());
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
		
		return sql;
	}

	protected void addColumnToken(InsertSql sql,
			JdbcParameterSource parameterSource, TableColumn column,
			String propertyName, Object value) {
		String columnName = column.getColumnName();
		JdbcType jdbcType = column.getJdbcType();
		if (jdbcType != null) {
			Object dbValue = jdbcType.toDB(value);
			parameterSource.setValue(propertyName, dbValue, jdbcType.getJdbcCode());
		}
		sql.addColumnToken(columnName, ":"+propertyName);
	}

	@Override
	public UpdateSql updateSql(Table table, Record record, JdbcDataResolverContext jdbcContext) {
		UpdateSql sql = new UpdateSql();
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(record);
		sql.setParameterSource(parameterSource);
		sql.setTableToken(SqlUtils.token(table));
		
		for (TableKeyColumn keyColumn: table.getKeyColumns()) {
			String propertyName = keyColumn.getPropertyName();
			Assert.notEmpty(propertyName, "propertyName of KeyColumn named [" + keyColumn.getColumnName() +"] must not be empty.");
			
			String columnName = keyColumn.getColumnName();
			sql.addKeyToken(columnName, ":"+propertyName);
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
		
		return sql;
	}

	protected void addColumnToken(UpdateSql sql, JdbcParameterSource parameterSource, 
			TableColumn c, String propertyName, Object value) {
		String columnName = c.getColumnName();
		JdbcType jdbcType = c.getJdbcType();
		if (jdbcType != null) {
			Object dbValue = jdbcType.toDB(value);
			parameterSource.setValue(propertyName, dbValue, jdbcType.getJdbcCode());
		}
		sql.addColumnToken(columnName, ":"+propertyName);
	}

	@Override
	public DeleteSql deleteSql(Table table, Record record, JdbcDataResolverContext jdbcContext) {
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
