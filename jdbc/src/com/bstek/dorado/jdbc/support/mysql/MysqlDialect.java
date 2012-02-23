package com.bstek.dorado.jdbc.support.mysql;

import com.bstek.dorado.jdbc.JdbcSpace;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsModel;
import com.bstek.dorado.jdbc.support.AbstractDialect;

/**
 * mysql database
 * @author mark.li@bstek.com
 * @see <a href='http://dev.mysql.com/doc/refman/5.5/en/sql-syntax.html'>http://dev.mysql.com/doc/refman/5.5/en/sql-syntax.html</a>
 */
public class MysqlDialect extends AbstractDialect {

	@Override
	public boolean isNarrowSupport() {
		return true;
	}

	@Override
	public String narrowSql(SelectSql selectSql, int maxResults, int firstResult) {
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
	
	@Override
	public boolean isSequenceSupport() {
		return false;
	}

	@Override
	public String sequenceSql(String sequenceName) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String token(NullsModel nullsModel) {
		return null;
	}

	@Override
	public JdbcSpace getTableJdbcSpace() {
		return JdbcSpace.CATALOG;
	}
}
