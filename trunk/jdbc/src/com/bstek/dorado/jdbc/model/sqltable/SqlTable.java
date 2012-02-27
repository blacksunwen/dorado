package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.AbstractColumn;
import com.bstek.dorado.jdbc.model.AbstractTable;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	parser = "spring:dorado.jdbc.sqlTableParser",
	definitionType="com.bstek.dorado.jdbc.model.sqltable.SqlTableDefinition",
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
	
	private String mainTableName;

	private Table mainTable;
	
	public void addColumn(AbstractColumn column) {
		if (column instanceof SqlTableColumn) {
			super.addColumn(column);
		} else {
			throw new IllegalArgumentException("unknown column class " + column.getClass());
		}
	}
	
	@IdeProperty(highlight=1, editor = "multiLines")
	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}
	
	@IdeProperty(highlight=1, editor="jdbc:refrence:Table")
	public String getMainTable() {
		return mainTableName;
	}

	public void setMainTable(String table) {
		this.mainTableName = table;
	}

	@Override
	public String getType() {
		return TYPE;
	}
	
	public Table getMainTableObject() {
		if (mainTable == null) {
			String tableName = this.getMainTable();
			Assert.notEmpty(tableName, "mainTable must not be null. [" + tableName + "]");
			
			mainTable = (Table)JdbcUtils.getDbTable(tableName);
		}
		
		return mainTable;
	}
	
	@Override
	protected String getDefaultSQLGeneratorServiceName() {
		return "spring:dorado.jdbc.sqlTableSqlGenerator";
	}
	
}