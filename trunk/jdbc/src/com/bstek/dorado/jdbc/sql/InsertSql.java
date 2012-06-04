package com.bstek.dorado.jdbc.sql;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class InsertSql extends AbstractTableSql {

	private List<String> columnNameList = new ArrayList<String>();
	private List<String> propertyNameList = new ArrayList<String>();
	
	private String[] columnNames = null;
	private String[] propertyNames = null;
	
	private TableKeyColumn identityColumn;
	private boolean retrieveAfterExecute = false;
	
	public InsertSql() {
		super();
	}
	
	public void addColumnToken(String columnName, String propertyName) {
		columnNameList.add(columnName);
		propertyNameList.add(propertyName);
	}
	
	public TableKeyColumn getIdentityColumn() {
		return identityColumn;
	}

	public void setIdentityColumn(TableKeyColumn identityColumn) {
		Assert.isNull(this.identityColumn, "already has IDENTITY column.");
		this.identityColumn = identityColumn;
	}
	
	public String[] getPropertyNames() {
		if (propertyNames == null) {
			propertyNames = propertyNameList.toArray(new String[propertyNameList.size()]);
		}
		return propertyNames;
	}

	public String[] getColumnNames() {
		if (columnNames == null) {
			columnNames = columnNameList.toArray(new String[columnNameList.size()]);
		}
		return columnNames;
	}

	public boolean isRetrieveAfterExecute() {
		return retrieveAfterExecute;
	}

	public void setRetrieveAfterExecute(boolean retrieveAfterExecute) {
		this.retrieveAfterExecute = retrieveAfterExecute;
	}
	
	@Override
	protected String toSQL(Dialect dialect) {
		String tableToken = this.getTableToken();
		Assert.notEmpty(tableToken, "tableToken must not be empty.");
		Assert.notEmpty(columnNameList, "columnNameList must not be empty.");
		Assert.notEmpty(propertyNameList, "propertyNameList must not be empty.");

		String[] columnNames = this.getColumnNames();
		String[] propertyNames = this.getPropertyNames();
		
		SqlBuilder sql = new SqlBuilder();
		sql.rightSpace(KeyWord.INSERT_INTO, tableToken).
			bracktsColumn(columnNames).bothSpace(KeyWord.VALUES).bracketsVar(propertyNames);
		return sql.build();
	}

}
