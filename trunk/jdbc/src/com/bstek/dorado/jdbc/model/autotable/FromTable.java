package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.jdbc.model.table.Table;

@XmlNode(
	definitionType = "com.bstek.dorado.jdbc.model.autotable.FromTableDefinition",
	properties = {
		@XmlProperty(
			propertyName = "table", 
			parser = "spring:dorado.jdbc.tableReferenceParser"
		)
	}
)
public class FromTable {

	private String tableAlias;
	
	private Table table;
	
	public String getTableAlias() {
		return this.tableAlias;
	}
	
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}
	
	@XmlProperty(parser = "spring:dorado.jdbc.tableReferenceParser")
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
	
}
