package com.bstek.dorado.jdbc.model.table;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlBuilder;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableSelectSql extends SelectSql {
	private String columnsToken;
	private String tableToken;
	private String dynamicToken;
	
	public String getColumnsToken() {
		return columnsToken;
	}

	public void setColumnsToken(String columnsToken) {
		this.columnsToken = columnsToken;
	}

	public String getTableToken() {
		return tableToken;
	}

	public void setTableToken(String fromToken) {
		this.tableToken = fromToken;
	}

	public String getDynamicToken() {
		return dynamicToken;
	}

	public void setDynamicToken(String dynamicToken) {
		this.dynamicToken = dynamicToken;
	}
	
	public String toSQL(Dialect dialect) {
		Assert.notEmpty(tableToken, "FromToken must not be empty.");
		SqlBuilder sql = new SqlBuilder();
		sql.rightSpace(KeyWord.SELECT, columnsToken, KeyWord.FROM).append(tableToken);
		
		if (StringUtils.isNotBlank(dynamicToken)) {
			sql.leftSpace(dynamicToken);
		}
		
		return sql.build();
	}

}
