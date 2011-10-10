package com.bstek.dorado.jdbc.model.table;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.BothSpace;
import com.bstek.dorado.jdbc.sql.SqlConstants.RightSpace;
import com.bstek.dorado.util.Assert;

public class TableSelectSql extends SelectSql {
	private String columnsToken;
	private String fromToken;
	private String dynamicToken;
	
	public String getColumnsToken() {
		return columnsToken;
	}

	public void setColumnsToken(String columnsToken) {
		this.columnsToken = columnsToken;
	}

	public String getFromToken() {
		return fromToken;
	}

	public void setFromToken(String fromToken) {
		this.fromToken = fromToken;
	}

	public String getDynamicToken() {
		return dynamicToken;
	}

	public void setDynamicToken(String dynamicToken) {
		this.dynamicToken = dynamicToken;
	}
	
	public String toSQL(Dialect dialect) {
		StringBuilder sql = new StringBuilder();
		sql.append(RightSpace.SELECT + columnsToken);
		
		String fromToken = this.getFromToken();
		Assert.notEmpty(fromToken);
		sql.append(BothSpace.FROM + fromToken);
		
		String dynamicToken = this.getDynamicToken();
		if (StringUtils.isNotEmpty(dynamicToken)) {
			sql.append(' ').append(dynamicToken);
		}
		
		return sql.toString();
	}

	@Override
	public String toCountSQL(Dialect dialect) {
		StringBuilder sql = new StringBuilder();
		sql.append(RightSpace.SELECT + "COUNT(*)");
		
		String fromToken = this.getFromToken();
		Assert.notEmpty(fromToken);
		sql.append(BothSpace.FROM + fromToken);
		
		String dynamicToken = this.getDynamicToken();
		if (StringUtils.isNotEmpty(dynamicToken)) {
			sql.append(' ').append(dynamicToken);
		}
		
		return sql.toString();
	}
}
