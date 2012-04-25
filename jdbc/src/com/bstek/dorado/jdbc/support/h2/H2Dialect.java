package com.bstek.dorado.jdbc.support.h2;

import com.bstek.dorado.jdbc.JdbcSpace;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.support.AbstractDialect;
import com.bstek.dorado.util.Assert;

/**
 * h2 database
 * @author mark.li@bstek.com
 * @see <a href='http://www.h2database.com/html/grammar.html#table_expression'>http://www.h2database.com/html/grammar.html#table_expression</a>
 */
public class H2Dialect extends AbstractDialect {

	public H2Dialect() {
		super();
	}
	
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
	
	public boolean isSequenceSupport() {
		return true;
	}
	
	public String sequenceSql(String sequenceName) {
		Assert.notNull(sequenceName, "'sequenceName' can not be empty.");
		return "SELECT NEXT VALUE FOR " + sequenceName;
	}

	public JdbcSpace getTableJdbcSpace() {
		return JdbcSpace.SCHEMA;
	}
	
}
