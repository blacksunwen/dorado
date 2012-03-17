package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.JdbcParameterSource;

@XmlNode (
	nodeName = "Sql"
)
public class SqlMatchRule extends BaseMatchRule {

	private String sql;
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String token(AutoTable autoTable, JdbcParameterSource parameterSource) {
		return sql;
	}

}
