package com.bstek.dorado.jdbc.model.sqltable;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.util.Assert;

public class SqlTableSqlGenerator implements SqlGenerator {

	public DbElement.Type getType() {
		return DbElement.Type.SqlTable;
	}
	
	@Override
	public SelectSql selectSql(JdbcDataProviderOperation operation) {
		SqlTable t = (SqlTable)operation.getDbElement();
		Object parameter = operation.getJdbcContext().getParameter();
		SqlSelectSql selectSql = new SqlSelectSql();
		
		//querySql
		String querySql = t.getQuerySql();
		querySql = SqlUtils.build(querySql, parameter);
		selectSql.setDynamicToken(querySql);
		
		//SqlParameterSource
		JdbcParameterSource p = SqlUtils.createJdbcParameter(parameter);
		selectSql.setParameterSource(p);
		return selectSql;
	}

	@Override
	public InsertSql insertSql(JdbcRecordOperation operation) {
		SqlTable sqlTable = (SqlTable)operation.getDbElement();
		Table table = sqlTable.getTable();
		Assert.notNull(table, sqlTable.getType() + " [" + sqlTable.getName() + "] " + "has no table to be inserted into.");
		
		Record record = operation.getRecord();
		Record sRecord = new Record();
		for (Column c: sqlTable.getAllColumns()) {
			SqlTableColumn column = (SqlTableColumn)c;
			if (column.isInsertable()) {
				String columnName = column.getResolveColumnName();
				if (StringUtils.isEmpty(columnName)) {
					columnName = column.getColumnName();
				}
				Column tableColumn = table.getColumn(columnName);
				String propertyName = tableColumn.getPropertyName();
				if (StringUtils.isNotEmpty(propertyName)) {
					Object value = record.get(column.getPropertyName());
					sRecord.put(propertyName, value);
				}
			}
		}
		
		JdbcRecordOperation sOperation = new JdbcRecordOperation(table, sRecord, operation.getJdbcContext());
		SqlGenerator generator = JdbcUtils.getSqlGenerator(table);
		operation.setSubstitute(sOperation);
		return generator.insertSql(sOperation);
	}

	@Override
	public UpdateSql updateSql(JdbcRecordOperation operation) {
		SqlTable sqlTable = (SqlTable)operation.getDbElement();
		Table table = sqlTable.getTable();
		Assert.notNull(table, sqlTable.getType() + " [" + sqlTable.getName() + "] " + "has no table to be updated.");
		
		Record record = operation.getRecord();
		Record sRecord = new Record();
		for (Column c: sqlTable.getAllColumns()) {
			SqlTableColumn column = (SqlTableColumn)c;
			if (column.isUpdatable()) {
				String columnName = column.getResolveColumnName();
				if (StringUtils.isEmpty(columnName)) {
					columnName = column.getColumnName();
				}
				Column tableColumn = table.getColumn(columnName);
				String propertyName = tableColumn.getPropertyName();
				if (StringUtils.isNotEmpty(propertyName)) {
					Object value = record.get(column.getPropertyName());
					sRecord.put(propertyName, value);
				}
			}
		}
		
		JdbcRecordOperation sOperation = new JdbcRecordOperation(table, sRecord, operation.getJdbcContext());
		SqlGenerator generator = JdbcUtils.getSqlGenerator(table);
		operation.setSubstitute(sOperation);
		return generator.updateSql(sOperation);
	}

	@Override
	public DeleteSql deleteSql(JdbcRecordOperation operation) {
		SqlTable sqlTable = (SqlTable)operation.getDbElement();
		Table table = sqlTable.getTable();
		Assert.notNull(table, sqlTable.getType() + " [" + sqlTable.getName() + "] " + "has no table to be updated.");
		
		Record record = operation.getRecord();
		Record sRecord = new Record();
		for (Column c: sqlTable.getAllColumns()) {
			SqlTableColumn column = (SqlTableColumn)c;
			if (column.isInsertable()) {
				String columnName = column.getResolveColumnName();
				if (StringUtils.isEmpty(columnName)) {
					columnName = column.getColumnName();
				}
				Column tableColumn = table.getColumn(columnName);
				String propertyName = tableColumn.getPropertyName();
				if (StringUtils.isNotEmpty(propertyName)) {
					Object value = record.get(column.getPropertyName());
					sRecord.put(propertyName, value);
				}
			}
		}
		
		JdbcRecordOperation sOperation = new JdbcRecordOperation(table, sRecord, operation.getJdbcContext());
		SqlGenerator generator = JdbcUtils.getSqlGenerator(table);
		operation.setSubstitute(sOperation);
		return generator.deleteSql(sOperation);
	}

}
