package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.jdbc.Dialect;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class SelectSql extends AbstractSql {
	
	public String toCountSQL(Dialect dialect) {
		String sql = this.toSQL(dialect);
		String countSql = dialect.toCountSQL(sql);
		return countSql;
	}
}
