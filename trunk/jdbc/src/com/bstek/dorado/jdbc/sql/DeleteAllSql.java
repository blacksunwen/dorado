package com.bstek.dorado.jdbc.sql;

import java.util.LinkedHashMap;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.util.Assert;

public class DeleteAllSql extends AbstractTableSql {
	
	private LinkedHashMap<String, String> columnTokenMap = new LinkedHashMap<String, String>();
	
	public void addColumnToken(String columnName, String value) {
		columnTokenMap.put(columnName, value);
	}
	
	@Override
	public String toSQL(Dialect dialect) throws Exception {
		String tableToken = this.getTableToken();
		Assert.notEmpty(tableToken, "tableToken must not be empty.");
		Assert.notEmpty(columnTokenMap, "columnTokenMap must not be empty.");
		
		SqlBuilder sql = new SqlBuilder();
		sql.rightSpace(KeyWord.DELETE, KeyWord.FROM, tableToken, KeyWord.WHERE);
		
		String[] keyArray = columnTokenMap.keySet().toArray(new String[0]);
		for (int i = 0; i < keyArray.length; i++) {
			String key = keyArray[i];
			String value = columnTokenMap.get(key);
			if (i > 0) {
				sql.bothSpace(KeyWord.AND);
			}
			
			sql.append(key, "=", value);
		}
		return sql.build();
	}

}
