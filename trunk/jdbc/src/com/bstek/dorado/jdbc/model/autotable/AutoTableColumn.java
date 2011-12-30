package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.model.table.TableColumn;
import com.bstek.dorado.util.Assert;

@XmlNode(
	nodeName="Column",
	definitionType = "com.bstek.dorado.jdbc.model.ColumnDefinition"
)
public class AutoTableColumn extends TableColumn {

	private String tableAlias;
	private String columnAlias;
	
	private AutoTable autoTable;
	
	public FromTable getFromTable() {
		Assert.notNull(autoTable);
		Assert.notEmpty(tableAlias);
		
		return autoTable.getFromTable(tableAlias);
	}

	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public AutoTable getAutoTable() {
		return autoTable;
	}

	public void setAutoTable(AutoTable autoTable) {
		this.autoTable = autoTable;
	}
	
	public String getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
	}
}

