package com.bstek.dorado.jdbc.support.derby;

import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;
import com.bstek.dorado.jdbc.support.AbstractDialect;

/**
 * derby database
 * @author mark
 * @see <a href='http://db.apache.org/derby/docs/10.8/ref/'>http://db.apache.org/derby/docs/10.8/ref/</a>
 */
public class DerbyDialect extends AbstractDialect {

	@Override
	public boolean isNarrowSupport() {
		return true;
	}

	/**
	 * @see <a href='http://db.apache.org/derby/docs/10.8/ref/rrefsqljoffsetfetch.html#rrefsqljoffsetfetch'>http://db.apache.org/derby/docs/10.8/ref/rrefsqljoffsetfetch.html#rrefsqljoffsetfetch</a>
	 * @see com.bstek.dorado.jdbc.Dialect#narrowSql(com.bstek.dorado.jdbc.sql.SelectSql, int, int)
	 */
	@Override
	public String narrowSql(SelectSql selectSql, int maxResults, int firstResult) {
		String sql = this.toSQL(selectSql);
		if (firstResult <= 0) {
			return sql + " FETCH NEXT " + maxResults + " ROWS ONLY";
		} else {
			return sql + " OFFSET " + firstResult + " FETCH NEXT " + maxResults + " ROWS ONLY";
		}
	}

	@Override
	public boolean isSequenceSupport() {
		return true;
	}

	/**
	 * @see <a href='http://db.apache.org/derby/docs/10.8/ref/rrefsqljnextvaluefor.html'>http://db.apache.org/derby/docs/10.8/ref/rrefsqljnextvaluefor.html</a>
	 */
	@Override
	public String sequenceSql(String sequenceName) {
		return "VALUES NEXT VALUE FOR " + sequenceName;
	}

	/**
	 * @see <a href='http://db.apache.org/derby/docs/10.8/ref/rrefsqlj29840.html'>http://db.apache.org/derby/docs/10.8/ref/rrefsqlj29840.html</a>
	 */
	@Override
	public String token(JoinModel joinModel) {
		switch (joinModel) {
		case INNER_JOIN:
			return "INNER JOIN";
		case LEFT_JOIN:
			return "LEFT OUTER JOIN";
		case RIGHT_JOIN:
			return "RIGHT OUTER JOIN";
		}
		
		throw new IllegalArgumentException("unknown JoinModel '" + joinModel + "'");
	}

}
