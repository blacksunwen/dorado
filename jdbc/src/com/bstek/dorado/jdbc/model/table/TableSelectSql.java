package com.bstek.dorado.jdbc.model.table;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.VarSql;
import com.bstek.dorado.jdbc.sql.SqlBuilder;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableSelectSql extends SelectSql {
	private String columnsToken;
	private String tableToken;
	private String dynamicToken;
	
	private Object parameter;
	
	public String getColumnsToken() {
		return columnsToken;
	}

	public void setColumnsToken(String columnsToken) {
		this.columnsToken = columnsToken;
	}

	public String getTableToken() {
		return tableToken;
	}

	public void setTableToken(String fromToken) {
		this.tableToken = fromToken;
	}

	public String getDynamicToken() {
		return dynamicToken;
	}

	public void setDynamicToken(String dynamicToken) {
		this.dynamicToken = dynamicToken;
	}
	
	public Object getParameter() {
		if (parameter == null) {
			parameter = new HashMap<Object, Object>();
		}
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
//		this.setParameterSource(SqlUtils.createJdbcParameter(parameter));
	}

	@Override
	protected String doBuild(Dialect dialect) throws Exception {
		Assert.notEmpty(tableToken, "tableToken must not be empty.");
		SqlBuilder builder = new SqlBuilder();
		builder.rightSpace(KeyWord.SELECT, columnsToken, KeyWord.FROM).append(tableToken);
		
		if (StringUtils.isNotBlank(dynamicToken)) {
			VarSql sql = SqlUtils.build(dynamicToken, getParameter());
			this.setParameterSource(sql.getParameterSource());
			String clause = sql.getClause();
			builder.leftSpace(clause);
		}
		
		String sql = builder.build();
		return sql;
	}
}
