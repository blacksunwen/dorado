package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.jdbc.Dialect;

public abstract class SelectSql extends AbstractSql {
	
	public String toCountSQL(Dialect dialect) {
		String sql = this.toSQL(dialect);
		String countSql = dialect.toCountSQL(sql);
		return countSql;
	}
}
