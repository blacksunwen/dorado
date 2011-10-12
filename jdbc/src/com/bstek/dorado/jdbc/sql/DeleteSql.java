package com.bstek.dorado.jdbc.sql;

import java.util.LinkedHashMap;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.util.Assert;

public class DeleteSql extends AbstractTableSql {
	
	private LinkedHashMap<String, String> keyTokenMap = new LinkedHashMap<String, String>(2);
	
	public void addKeyToken(String columnName, String value) {
		keyTokenMap.put(columnName, value);
	}
	
	@Override
	public String toSQL(Dialect dialect) {
		String tableToken = this.getTableToken();
		Assert.notEmpty(tableToken, "tableToken must not be empty.");
		Assert.notEmpty(keyTokenMap, "keyTokenMap must not be empty.");
		
		SqlBuilder sql = new SqlBuilder();
		sql.rightSpace(KeyWord.DELETE, KeyWord.FROM, tableToken, KeyWord.WHERE);
		
		String[] keyArray = keyTokenMap.keySet().toArray(new String[0]);
		for (int i = 0; i < keyArray.length; i++) {
			String key = keyArray[i];
			String value = keyTokenMap.get(key);
			if (i > 0) {
				sql.bothSpace(KeyWord.AND);
			}
			
			sql.append(key, "=", value);
		}
		return sql.build();
	}

}
