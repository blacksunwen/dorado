package com.bstek.dorado.jdbc.model.table;

import java.util.List;

import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.SqlUtils;

public class TableSqlGenerator implements SqlGenerator<Table> {

	public DbElement.Type getType() {
		return DbElement.Type.Table;
	}
	
	@Override
	public SelectSql selectSql(Table t, Object parameter) {
		//SelectSql
		TableSelectSql selectSql = new TableSelectSql();
				
		//columnsToken
		StringBuilder columnsToken = new StringBuilder();
		List<Column> columns = t.getAllColumns();
		for (int i=0, j=columns.size(), ableColumnCount = 0; i<j; i++) {
			Column column = columns.get(i);
			if (column.isSelectable()) {
				if (ableColumnCount++ > 0) {
					columnsToken.append(',');
				}
				
				String token = column.getColumnName();
				columnsToken.append(token);
			}
		}
		selectSql.setColumnsToken(columnsToken.toString());
		
		//fromToken
		String fromToken = SqlUtils.token(t);
		selectSql.setFromToken(fromToken);
		
		//dynamicToken
		String dynamicToken = t.getDynamicToken();
		dynamicToken = SqlUtils.build(dynamicToken, parameter);
		
		selectSql.setDynamicToken(dynamicToken);
		
		//JdbcParameterSource
		JdbcParameterSource p = SqlUtils.createJdbcParameter(parameter);
		selectSql.setParameterSource(p);
		
		return selectSql;
	}

}
