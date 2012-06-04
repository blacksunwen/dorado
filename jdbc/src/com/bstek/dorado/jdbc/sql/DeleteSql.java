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
public class DeleteSql extends AbstractTableSql {
	
	private List<String> columnNameList = new ArrayList<String>(3);
	private List<String> propertyNameList = new ArrayList<String>(3);
	
	public void addKeyToken(String columnName, String propertyName) {
		columnNameList.add(columnName);
		propertyNameList.add(propertyName);
	}
	
	@Override
	protected String toSQL(Dialect dialect) {
		String tableToken = this.getTableToken();
		Assert.notEmpty(tableToken, "tableToken must not be empty.");
		Assert.notEmpty(columnNameList, "columnNameList must not be empty.");
		
		SqlBuilder sql = new SqlBuilder();
		sql.rightSpace(KeyWord.DELETE, KeyWord.FROM, tableToken, KeyWord.WHERE);
		
		for (int i=0; i<columnNameList.size(); i++) {
			String columnName = columnNameList.get(i);
			String propertyName = propertyNameList.get(i);
			
			if (i > 0) {
				sql.bothSpace(KeyWord.AND);
			}
			
			sql.append(columnName, "=", ":"+propertyName);
		}
		return sql.build();
	}

}
