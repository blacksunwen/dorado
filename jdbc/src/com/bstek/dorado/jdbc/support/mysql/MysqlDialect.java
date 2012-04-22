package com.bstek.dorado.jdbc.support.mysql;

import com.bstek.dorado.jdbc.JdbcSpace;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsDirection;
import com.bstek.dorado.jdbc.support.AbstractDialect;

/**
 * mysql database
 * @author mark.li@bstek.com
 * @see <a href='http://dev.mysql.com/doc/refman/5.5/en/sql-syntax.html'>http://dev.mysql.com/doc/refman/5.5/en/sql-syntax.html</a>
 */
public class MysqlDialect extends AbstractDialect {

	public boolean isNarrowSupport() {
		return true;
	}

	public String narrowSql(SelectSql selectSql, int maxResults, int firstResult) throws Exception {
		String sql = this.toSQL(selectSql);
		
		if (firstResult <= 0) {
			return sql + " LIMIT " + maxResults;
		} else {
			return sql + " LIMIT " + maxResults + " OFFSET " + firstResult;
		}
	}

	@Override
	public String toCountSQL(String sql) {
		return "SELECT COUNT(1) FROM ( " + sql + " ) AS _CT_";
	}
	
	public boolean isSequenceSupport() {
		return false;
	}

	public String sequenceSql(String sequenceName) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String token(NullsDirection nullsModel) {
		return null;
	}

	public JdbcSpace getTableJdbcSpace() {
		return JdbcSpace.CATALOG;
	}
}
