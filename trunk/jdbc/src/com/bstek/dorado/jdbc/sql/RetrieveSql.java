package com.bstek.dorado.jdbc.sql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.util.Assert;

public class RetrieveSql extends AbstractTableSql {

	private List<String> columnTokenList = new ArrayList<String>();
	private List<String> aliasTokenList = new ArrayList<String>();
	
	private LinkedHashMap<String, String> keyTokenMap = new LinkedHashMap<String, String>(2);
	
	public void addColumnToken(String columnName, String alias) {
		columnTokenList.add(columnName);
		aliasTokenList.add(alias);
	}
	
	public void addColumnToken(String columnName) {
		this.addColumnToken(columnName, null);
	}
	
	public void addKeyToken(String columnName, String value) {
		keyTokenMap.put(columnName, value);
	}
	
	@Override
	public String toSQL(Dialect dialect) {
		String tableToken = this.getTableToken();
		Assert.notEmpty(tableToken, "tableToken must not be empty.");
		Assert.notEmpty(columnTokenList, "columnTokenList must not be empty.");
		Assert.notEmpty(aliasTokenList, "aliasTokenList must not be empty.");
		Assert.notEmpty(keyTokenMap, "keyTokenMap must not be empty.");
		
		List<String> cList = new ArrayList<String>(columnTokenList.size());
		for (int i=0; i<columnTokenList.size(); i++) {
			String column = columnTokenList.get(i);
			String alias = aliasTokenList.get(i);
			
			if (StringUtils.isEmpty(alias)) {
				cList.add(column);
			} else {
				cList.add(column + " " + KeyWord.AS + " " + alias);
			}
		}
		String columnsToken = StringUtils.join(cList, ',');
		
		String whereToken;
		if (keyTokenMap.size() == 1) {
			String key = keyTokenMap.keySet().iterator().next();
			String value = keyTokenMap.get(key);
			whereToken = key + "=" + value;
		} else {
			String[] keyArray = keyTokenMap.keySet().toArray(new String[0]);
			for (int i = 0; i < keyArray.length; i++) {
				String key = keyArray[i];
				String value = keyTokenMap.get(key);
				keyArray[i] = key + "=" + value;
			}
			
			whereToken = StringUtils.join(keyArray, " " + KeyWord.AND + " ");
		}
		
		SqlBuilder sql = new SqlBuilder();
		sql.rightSpace(KeyWord.SELECT, columnsToken, KeyWord.FROM, tableToken, KeyWord.WHERE).append(whereToken);
		
		return sql.build();
	}

}
