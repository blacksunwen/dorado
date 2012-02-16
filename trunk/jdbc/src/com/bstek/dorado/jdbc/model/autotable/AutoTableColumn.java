package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.model.AbstractUpdatableColumn;
import com.bstek.dorado.util.Assert;

@XmlNode(
	nodeName="Column",
	definitionType = "com.bstek.dorado.jdbc.config.ColumnDefinition"
)
public class AutoTableColumn extends AbstractUpdatableColumn {

	private String nativeColumnName;
	
	private String tableAlias;
	
	private AutoTable autoTable;

	public String getNativeColumnName() {
		return nativeColumnName;
	}

	public void setNativeColumnName(String nativeColumnName) {
		this.nativeColumnName = nativeColumnName;
	}
	
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
	
}

