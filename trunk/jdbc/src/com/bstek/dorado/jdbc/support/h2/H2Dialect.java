package com.bstek.dorado.jdbc.support.h2;

import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;
import com.bstek.dorado.jdbc.support.AbstractDialect;
import com.bstek.dorado.util.Assert;

public class H2Dialect extends AbstractDialect {

	@Override
	public boolean isNarrowSupport() {
		return true;
	}
	
	@Override
	public String narrowSql(SelectSql selectSql, int maxResults, int firstResult) {
		String sql = this.toSQL(selectSql);
		
		if (firstResult <= 0) {
			return sql + " limit " + maxResults;
		} else {
			return sql + " limit " + maxResults + " offset " + firstResult;
		}
	}
	
	@Override
	public boolean isSequenceSupport() {
		return true;
	}
	
	@Override
	public String sequenceSql(String sequenceName) {
		Assert.notNull(sequenceName, "'sequenceName' can not be empty.");
		return "SELECT NEXT VALUE FOR " + sequenceName;
	}
	
	@Override
	protected String token(JoinModel joinModel) {
		switch (joinModel) {
		case LEFT_JOIN:
			return "LEFT JOIN";
		case RIGHT_JOIN:
			return "RIGHT JOIN";
		case INNER_JOIN:
			return "INNER JOIN";
		}
		
		throw new IllegalArgumentException("unknown JoinModel [" + joinModel + "]");
	}
}
