package com.bstek.dorado.jdbc.model.sqltable;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.util.Assert;

public class SqlTableSqlGenerator implements SqlGenerator {

	public String getType() {
		return "SqlTable";
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
		Map<String, String> propertyMap = new HashMap<String, String>();
		for (Column c: sqlTable.getAllColumns()) {
			SqlTableColumn column = (SqlTableColumn)c;
			String columnName = column.getNativeColumnName();
			String propertyName = column.getPropertyName();
			if (StringUtils.isNotEmpty(columnName) && StringUtils.isNotEmpty(propertyName)) {
				if (column.isInsertable()) {
					Column tableColumn = table.getColumn(columnName);
					String tpn = tableColumn.getPropertyName();
					if (StringUtils.isNotEmpty(tpn)) {
						Object value = record.get(propertyName);
						sRecord.put(tpn, value);
						propertyMap.put(propertyName, tpn);
					}
				}
			}
		}
		
		JdbcRecordOperation sOperation = new JdbcRecordOperation(table, sRecord, operation.getJdbcContext());
		SqlGenerator generator = JdbcUtils.getSqlGenerator(table);
		operation.setSubstitute(sOperation, propertyMap);
		return generator.insertSql(sOperation);
	}

	@Override
	public UpdateSql updateSql(JdbcRecordOperation operation) {
		SqlTable sqlTable = (SqlTable)operation.getDbElement();
		Table table = sqlTable.getTable();
		Assert.notNull(table, sqlTable.getType() + " [" + sqlTable.getName() + "] " + "has no table to be updated.");
		
		Record record = operation.getRecord();
		Record sRecord = new Record();
		Map<String, String> propertyMap = new HashMap<String, String>();
		for (Column c: sqlTable.getAllColumns()) {
			SqlTableColumn column = (SqlTableColumn)c;
			String columnName = column.getNativeColumnName();
			String propertyName = column.getPropertyName();
			if (StringUtils.isNotEmpty(columnName) && StringUtils.isNotEmpty(propertyName)) {
				if (column.isUpdatable()) {
					Column tableColumn = table.getColumn(columnName);
					String sPropertyName = tableColumn.getPropertyName();
					if (StringUtils.isNotEmpty(sPropertyName)) {
						Object value = record.get(propertyName);
						sRecord.put(sPropertyName, value);
						propertyMap.put(propertyName, sPropertyName);
					}
				}
			}
		}
		
		JdbcRecordOperation sOperation = new JdbcRecordOperation(table, sRecord, operation.getJdbcContext());
		SqlGenerator generator = JdbcUtils.getSqlGenerator(table);
		operation.setSubstitute(sOperation, propertyMap);
		return generator.updateSql(sOperation);
	}

	@Override
	public DeleteSql deleteSql(JdbcRecordOperation operation) {
		SqlTable sqlTable = (SqlTable)operation.getDbElement();
		Table table = sqlTable.getTable();
		Assert.notNull(table, sqlTable.getType() + " [" + sqlTable.getName() + "] " + "has no table to be updated.");
		
		Record record = operation.getRecord();
		Record sRecord = new Record();
		Map<String, String> propertyMap = new HashMap<String, String>();
		for (Column c: sqlTable.getAllColumns()) {
			SqlTableColumn column = (SqlTableColumn)c;
			String columnName = column.getNativeColumnName();
			String propertyName = column.getPropertyName();
			if (StringUtils.isNotEmpty(columnName) && StringUtils.isNotEmpty(propertyName)) {
				if (column.isInsertable()) {
					Column tableColumn = table.getColumn(columnName);
					String sPropertyName = tableColumn.getPropertyName();
					if (StringUtils.isNotEmpty(sPropertyName)) {
						Object value = record.get(propertyName);
						sRecord.put(sPropertyName, value);
						propertyMap.put(propertyName, sPropertyName);
					}
				}
			}
		}
		
		JdbcRecordOperation sOperation = new JdbcRecordOperation(table, sRecord, operation.getJdbcContext());
		SqlGenerator generator = JdbcUtils.getSqlGenerator(table);
		operation.setSubstitute(sOperation, propertyMap);
		return generator.deleteSql(sOperation);
	}

}
