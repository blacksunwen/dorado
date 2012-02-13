package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.jdbc.model.AbstractTable;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.table.Table;

@XmlNode(
	definitionType="com.bstek.dorado.jdbc.model.sqltable.SqlTableDefinition",
	properties = {
		@XmlProperty(
			propertyName = "table", 
			parser = "spring:dorado.jdbc.tableReferenceParser"
		)
	},
	subNodes = {
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName = "Columns", fixed = true), 
			propertyName="Jdbc_SqlTableColumns",
			propertyType = "List<com.bstek.dorado.jdbc.model.sqltable.SqlTableColumn>"
		)
	}
)
public class SqlTable extends AbstractTable {

	public static final String TYPE = "SqlTable";
	
	private String querySql;
	
	private Table table;

	public void addColumn(Column column) {
		if (column instanceof SqlTableColumn) {
			super.addColumn(column);
		} else {
			throw new IllegalArgumentException("unknown column class " + column.getClass());
		}
	}
	
	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}
	
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	protected String getDefaultSQLGeneratorName() {
		return "spring:dorado.jdbc.sqlTableSqlGenerator";
	}
	
}