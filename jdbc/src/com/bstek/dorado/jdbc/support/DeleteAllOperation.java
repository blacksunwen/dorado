package com.bstek.dorado.jdbc.support;

import java.util.Map;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.table.Table;

public class DeleteAllOperation extends AbstractDbTableOperation<DefaultJdbcContext, Table> {

	private Map<String, Object> columnValueMap;
	
	public DeleteAllOperation(Table dbTable, DefaultJdbcContext jdbcContext, Map<String, Object> columnValueMap) {
		super(dbTable, jdbcContext);
		this.columnValueMap = columnValueMap;
	}

	public Map<String, Object> getColumnValueMap() {
		return columnValueMap;
	}

	@Override
	protected boolean doExecute() throws Exception {
		JdbcEnviroment jdbcEnviroment = getJdbcEnviroment();
		return jdbcEnviroment.getDialect().execute(this);
	}

}
