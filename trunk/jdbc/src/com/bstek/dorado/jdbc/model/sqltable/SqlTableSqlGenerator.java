package com.bstek.dorado.jdbc.model.sqltable;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.AbstractColumn;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.CurdSqlGenerator;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class SqlTableSqlGenerator implements CurdSqlGenerator {
	
	@Override
	public SelectSql selectSql(JdbcDataProviderOperation operation) {
		SqlTable t = (SqlTable)operation.getDbTable();
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
		JdbcRecordOperation sOperation = createOperation(operation, new OperationConfig() {
			@Override
			public boolean accept(SqlTableColumn column) {
				return column.isInsertable();
			}
		});
		
		SqlTable sqlTable = (SqlTable)operation.getDbTable();
		Table table = sqlTable.getMainTableObject();
		CurdSqlGenerator generator = table.getCurdSqlGenerator();
		return generator.insertSql(sOperation);
	}

	@Override
	public UpdateSql updateSql(JdbcRecordOperation operation) {
		JdbcRecordOperation sOperation = createOperation(operation, new OperationConfig() {
			@Override
			public boolean accept(SqlTableColumn column) {
				return column.isUpdatable();
			}
		});
		
		SqlTable sqlTable = (SqlTable)operation.getDbTable();
		Table table = sqlTable.getMainTableObject();
		CurdSqlGenerator generator = table.getCurdSqlGenerator();
		return generator.updateSql(sOperation);
	}

	@Override
	public DeleteSql deleteSql(JdbcRecordOperation operation) {
		JdbcRecordOperation sOperation = createOperation(operation, new OperationConfig() {
			@Override
			public boolean accept(SqlTableColumn column) {
				return true;
			}
		});
		
		SqlTable sqlTable = (SqlTable)operation.getDbTable();
		
		Table table = sqlTable.getMainTableObject();
		CurdSqlGenerator generator = table.getCurdSqlGenerator();
		return generator.deleteSql(sOperation);
	}
	
	interface OperationConfig {
		boolean accept(SqlTableColumn column);
	}
	
	protected JdbcRecordOperation createOperation(JdbcRecordOperation operation, OperationConfig config) {
		SqlTable sqlTable = (SqlTable)operation.getDbTable();
		Table table = sqlTable.getMainTableObject();
		
		Record record = operation.getRecord();
		Record sRecord = new Record();
		Map<String, String> propertyMap = new HashMap<String, String>();
		for (AbstractColumn c: sqlTable.getAllColumns()) {
			SqlTableColumn column = (SqlTableColumn)c;
			String natColumnName = column.getNativeColumnName();
			String propertyName = column.getPropertyName();
			if (StringUtils.isNotEmpty(natColumnName) && config.accept(column)) {
				AbstractColumn tableColumn = table.getColumn(natColumnName);
				String tpn = tableColumn.getPropertyName();
				if (StringUtils.isNotEmpty(tpn)) {
					Object value = record.get(propertyName);
					sRecord.put(tpn, value);
					propertyMap.put(propertyName, tpn);
				}
			}
		}
		
		JdbcRecordOperation sOperation = new JdbcRecordOperation(table, sRecord, operation.getJdbcContext());
		operation.setSubstitute(sOperation, propertyMap);
		
		return sOperation;
	}
}
