package com.bstek.dorado.jdbc.model.autotable;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlBuilder;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.util.Assert;

public class AutoTableSelectSql  extends SelectSql {
	private String columnsToken;
	private String fromToken;
	private String whereToken;
	private String orderToken;
	
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

	public String getWhereToken() {
		return whereToken;
	}

	public void setWhereToken(String whereToken) {
		this.whereToken = whereToken;
	}

	public String getOrderToken() {
		return orderToken;
	}

	public void setOrderToken(String orderToken) {
		this.orderToken = orderToken;
	}
	
	public String toSQL(Dialect dialect) {
		Assert.notEmpty(columnsToken, "ColumnsToken must not be empty.");
		Assert.notEmpty(fromToken, "FromToken must not be empty.");
		
		SqlBuilder sql = new SqlBuilder();
		
		sql.rightSpace(KeyWord.SELECT, columnsToken, KeyWord.FROM).append(fromToken);
		
		if (StringUtils.isNotEmpty(whereToken)) {
			sql.leftSpace(KeyWord.WHERE, whereToken);
		}
		
		if (StringUtils.isNotEmpty(orderToken)) {
			sql.leftSpace(KeyWord.ORDER_BY, orderToken);
		}
		
		return sql.build();
	}

	@Override
	public String toCountSQL(Dialect dialect) {
		Assert.notEmpty(fromToken, "FromToken must not be empty.");
		
		SqlBuilder sql = new SqlBuilder();
		sql.rightSpace(KeyWord.SELECT, "COUNT(1)", KeyWord.FROM).append(fromToken);
		
		if (StringUtils.isNotEmpty(whereToken)) {
			sql.leftSpace(KeyWord.WHERE, whereToken);
		}
		
		return sql.build();
	}
}
