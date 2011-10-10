package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.util.Assert;

public class SqlSelectSql  extends SelectSql {

	private String dynamicToken;
	
	public String getDynamicToken() {
		return dynamicToken;
	}
	public void setDynamicToken(String dynamicToken) {
		this.dynamicToken = dynamicToken;
	}
	
	public String toSQL(Dialect dialect) {
		return this.getDynamicToken();
	}
	
	@Override
	public String toCountSQL(Dialect dialect) {
		String dynamicToken = this.getDynamicToken();
		Assert.notEmpty(dynamicToken);
		
		String countSql = dialect.toCountSQL(dynamicToken);
		return countSql;
	}
}
