package com.bstek.dorado.jdbc.support.oracle;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.support.AbstractDialect;

/**
 * oracle database
 * @author mark
 * @see <a href='http://download.oracle.com/docs/cd/E11882_01/server.112/e26088/toc.htm'>http://download.oracle.com/docs/cd/E11882_01/server.112/e26088/toc.htm</a>
 */
public class OracleDialect extends AbstractDialect {

	@Override
	public String token(Table table, String alias) {
		String token = token(table);
		if (StringUtils.isEmpty(alias)) {
			return token;
		} else {
			return token + " "+ alias;
		}
	}

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

	@Override
	public String defaultSchema(DataSource dataSource,
			DatabaseMetaData databaseMetaData) {
		String schema = super.defaultSchema(dataSource, databaseMetaData);
		if (schema == null) {
			try {
				schema = databaseMetaData.getUserName();
			} catch (SQLException e) {
				throw new RuntimeException();
			}
		}
		return schema;
	}
	
}
