package com.bstek.dorado.jdbc.support.mssql;

import com.bstek.dorado.jdbc.JdbcSpace;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsDirection;
import com.bstek.dorado.jdbc.support.AbstractDialect;
import com.bstek.dorado.jdbc.support.JdbcConstants;

/**
 * sqlserver database
 * @author mark.li@bstek.com
 * @see <a href='http://technet.microsoft.com/en-us/library/ms173372.aspx'>http://technet.microsoft.com/en-us/library/ms173372.aspx</a>
 */
public class MssqlDialect extends AbstractDialect {
	
	public MssqlDialect() {
		super();
		this.setDefaultSchema("dbo");
	}

	public boolean isNarrowSupport() {
		return true;
	}

	public String narrowSql(SelectSql selectSql, int maxResults, int firstResult) throws Exception{
		String sql = this.toSQL(selectSql);
		if (firstResult <= 0) {
			String sql2 = sql.toUpperCase();
			int selectIndex = sql2.indexOf("SELECT");
			int selectDistinctIndex = sql2.indexOf("SELECT DISTINCT");
			int splitIndex = selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
			
			StringBuffer sSql = new StringBuffer(sql.length()+10);
			return sSql.append(sql).insert(splitIndex, " TOP " + maxResults).toString();
		} else {
			String orderby = "ORDER BY CURRENT_TIMESTAMP";
			StringBuilder b = new StringBuilder(sql.toUpperCase());
			int orderByIndex = b.indexOf("ORDER BY");
			if (orderByIndex > 0) {
				orderby = b.substring(orderByIndex);
				b.delete(orderByIndex, b.length());
			}
			
			int distinctIndex = b.indexOf(KeyWord.DISTINCT);
			if (distinctIndex > 0) {
				b.delete(distinctIndex, distinctIndex + KeyWord.DISTINCT.length() + 1);
				String select = b.substring(b.indexOf(KeyWord.SELECT) + KeyWord.SELECT.length(), b.indexOf(KeyWord.FROM));
				String groupFields = select.replaceAll("\\sAS[^,]+(,?)", "$1");
				b.append(" GROUP BY").append(groupFields);
			}
			
			int selectEndIndex = b.indexOf(KeyWord.SELECT) + KeyWord.SELECT.length();
			b.insert(selectEndIndex, " ROW_NUMBER() OVER (" + orderby + ") AS "+ JdbcConstants.ROWNUM_VAR + ",");
			
			b.insert(0, "WITH query AS (").append(") SELECT * FROM query ");
			b.append("WHERE ROWNUM_ BETWEEN " + firstResult + " AND " + (firstResult + maxResults));
			
			return b.toString();
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

	protected String token(NullsDirection nullsModel) {
		return null;
	}

	public JdbcSpace getTableJdbcSpace() {
		return JdbcSpace.SCHEMA;
	}

}
