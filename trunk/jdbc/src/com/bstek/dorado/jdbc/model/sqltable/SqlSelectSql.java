package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class SqlSelectSql  extends SelectSql {

	private String dynamicToken;
	private Object parameter;
	
	public String getDynamicToken() {
		return dynamicToken;
	}
	public void setDynamicToken(String dynamicToken) {
		this.dynamicToken = dynamicToken;
	}
	
	public Object getParameter() {
		return parameter;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
		this.setParameterSource(SqlUtils.createJdbcParameter(parameter));
	}
	
	@Override
	protected String doBuild(Dialect dialect) {
		Assert.notEmpty(dynamicToken, "DynamicToken must not be empty.");
		String sql = SqlUtils.build(dynamicToken, parameter);
		
		return sql;
	}
}
