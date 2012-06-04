package com.bstek.dorado.jdbc.sql;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class UpdateSql extends AbstractTableSql{
	private List<String> columnNameList = new ArrayList<String>();
	private List<String> propertyNameList = new ArrayList<String>();
	private String[] columnNames = null;
	private String[] propertyNames = null;
	
	private List<String> keyColumnNameList = new ArrayList<String>(3);
	private List<String> keyPropertyNameList = new ArrayList<String>(3);
	private String[] keyColumnNames = null;
	private String[] keyPropertyNames = null;
	
	private boolean retrieveAfterExecute = false;
	
	public void addColumnToken(String columnName, String propertyName) {
		columnNameList.add(columnName);
		propertyNameList.add(propertyName);
	}
	
	public void addKeyToken(String columnName, String propertyName) {
		keyColumnNameList.add(columnName);
		keyPropertyNameList.add(propertyName);
	}
	
	public String[] getColumnNames() {
		if (columnNames == null) {
			columnNames = columnNameList.toArray(new String[columnNameList.size()]);
		}
		return columnNames;
	}

	public String[] getPropertyNames() {
		if (propertyNames == null) {
			propertyNames = propertyNameList.toArray(new String[propertyNameList.size()]);
		}
		return propertyNames;
	}

	public String[] getKeyColumnNames() {
		if (keyColumnNames == null) {
			keyColumnNames = keyColumnNameList.toArray(new String[keyColumnNameList.size()]);
		}
		return keyColumnNames;
	}

	public String[] getKeyPropertyNames() {
		if (keyPropertyNames == null) {
			keyPropertyNames = keyPropertyNameList.toArray(new String[keyPropertyNameList.size()]);
		}
		return keyPropertyNames;
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
		Assert.notEmpty(keyColumnNameList, "keyColumnNameList must not be empty.");
		
		SqlBuilder sql = new SqlBuilder();
		sql.rightSpace(KeyWord.UPDATE).rightSpace(tableToken).rightSpace(KeyWord.SET);
		
		String[] columnNames = this.getColumnNames();
		String[] propertyNames = this.getPropertyNames();
		for(int i=0; i<columnNames.length; i++) {
			String columnName = columnNames[i];
			String propertyName = propertyNames[i];
			
			if (i > 0) {
				sql.append(",");
			}
			
			sql.append(columnName, "=", ":" + propertyName);
		}
		
		sql.bothSpace(KeyWord.WHERE);
		
		String[] keyColumnNames = this.getKeyColumnNames();
		String[] keyPropertyNames = this.getKeyPropertyNames();
		for(int i=0; i<keyColumnNames.length; i++) {
			String keyColumnName = keyColumnNames[i];
			String keyPropertyName = keyPropertyNames[i];
			
			if (i > 0) {
				sql.bothSpace(KeyWord.AND);
			}
			
			sql.append(keyColumnName, "=", ":" + keyPropertyName);
		}
		
		return sql.build();
	}

}
