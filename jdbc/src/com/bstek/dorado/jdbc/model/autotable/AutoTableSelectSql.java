package com.bstek.dorado.jdbc.model.autotable;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.BothSpace;
import com.bstek.dorado.jdbc.sql.SqlConstants.RightSpace;
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
		StringBuilder sql = new StringBuilder();
		String columnsToken = this.getColumnsToken();
		sql.append(RightSpace.SELECT + columnsToken);
		
		String fromToken = this.getFromToken();
		Assert.notEmpty(fromToken);
		sql.append(BothSpace.FROM + fromToken);
		
		String whereToken = this.getWhereToken();
		if (StringUtils.isNotEmpty(whereToken)) {
			sql.append(BothSpace.WHERE).append(whereToken);
		}
		
		String orderToken = this.getOrderToken();
		if (StringUtils.isNotEmpty(orderToken)) {
			sql.append(BothSpace.ORDER_BY).append(orderToken);
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
		
		String whereToken = this.getWhereToken();
		if (StringUtils.isNotEmpty(whereToken)) {
			sql.append(BothSpace.WHERE).append(whereToken);
		}
		
		return sql.toString();
	}
}
