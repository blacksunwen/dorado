package com.bstek.dorado.jdbc.support.oracle;

import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.support.AbstractDialect;

public class OracleDialect extends AbstractDialect {

	@Override
	public boolean isNarrowSupport() {
		return true;
	}

	@Override
	public String narrowSql(SelectSql selectSql, int maxResults, int firstResult) {
		String sql = this.toSQL(selectSql);
		if (firstResult <= 0) {
			return "SELECT * FROM ( " + sql + " ) WHERE ROWNUM <= " + maxResults;
		} else {
			return "SELECT * FROM ( " +
					"SELECT ROW_.*, ROWNUM " +JdbcConstants.ROWNUM_VAR + " FROM ( " + sql + " ) ROW_ WHERE ROWNUM <= " + (firstResult + maxResults) + 
					") WHERE "+JdbcConstants.ROWNUM_VAR+" > " + firstResult;
		}
	}

	@Override
	public boolean isSequenceSupport() {
		return true;
	}

	@Override
	public String sequenceSql(String sequenceName) {
		return "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
	}

}
