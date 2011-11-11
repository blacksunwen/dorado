package com.bstek.dorado.jdbc.support.db2;

import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsModel;
import com.bstek.dorado.jdbc.support.AbstractDialect;

/**
 * db2 database
 * @author mark
 * @see <a href='http://publib.boulder.ibm.com/infocenter/dzichelp/v2r2/index.jsp?topic=%2Fcom.ibm.db2.doc.sqlref%2Fbjnrspsh.htm'>http://publib.boulder.ibm.com/infocenter/dzichelp/v2r2/index.jsp?topic=%2Fcom.ibm.db2.doc.sqlref%2Fbjnrspsh.htm</a>
 */
public class Db2Dialect extends AbstractDialect {

	@Override
	public boolean isNarrowSupport() {
		return true;
	}

	@Override
	public String narrowSql(SelectSql selectSql, int maxResults, int firstResult) {
		String sql = this.toSQL(selectSql);
		if (firstResult <= 0) {
			return sql + " FETCH FIRST " + maxResults + " ROWS ONLY";
		} else {
			int startOfSelect = sql.toUpperCase().indexOf(KeyWord.SELECT);

			StringBuffer pagingSelect = new StringBuffer(sql.length() + 100)
					.append(sql.substring(0, startOfSelect))
					.append("SELECT * FROM ( SELECT ")
					.append(getRowNumberSql(sql));

			if (hasUnion(sql) || hasDistinct(sql)) {
				pagingSelect.append(" ROW_.* FROM ( ")
					.append(sql.substring(startOfSelect))
					.append(" ) AS ROW_");
			} else {
				pagingSelect.append(sql.substring(startOfSelect + 6));
			}

			pagingSelect.append(" ) AS TEMP_ WHERE " + JdbcConstants.ROWNUM_VAR + " ");
			pagingSelect.append("BETWEEN " + (firstResult + 1) + " AND " + (firstResult + maxResults));
			return pagingSelect.toString();
		}
	}

	protected String getRowNumberSql(String sql) {
		StringBuffer rownumber = new StringBuffer(50).append("ROWNUMBER() OVER(");

		int orderByIndex = sql.toUpperCase().indexOf("ORDER BY");
		if (orderByIndex > 0 && !(hasUnion(sql) || hasDistinct(sql))) {
			rownumber.append(sql.substring(orderByIndex));
		}
		rownumber.append(") AS " + JdbcConstants.ROWNUM_VAR + ",");
		return rownumber.toString();
	}
	
	protected boolean hasUnion(String sql) {
		int selectindex = sql.toUpperCase().indexOf("SELECT");
		if (selectindex >= 0) {
			return sql.toUpperCase().indexOf("UNION") >= 0;
		}
		return false;
	}
	
	protected boolean hasDistinct(String sql) {
		int selectindex = sql.toUpperCase().indexOf("SELECT");
		if (selectindex >= 0) {
			String tsql = sql.trim().substring(6).trim();
			if ((tsql.substring(0, 1).equals("*"))
					|| (tsql.substring(0, 8).equalsIgnoreCase("DISTINCT"))) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isSequenceSupport() {
		return true;
	}

	@Override
	public String sequenceSql(String sequenceName) {
		return "VALUES NEXTVAL FOR " + sequenceName;
	}

	@Override
	protected String token(NullsModel nullsModel) {
		return null;
	}

}
