package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.model.AbstractUpdatableColumn;

@XmlNode(
	nodeName="Column",
	definitionType = "com.bstek.dorado.jdbc.config.ColumnDefinition"
)
public class AutoTableColumn extends AbstractUpdatableColumn {

	private String nativeColumnName;
	
	private String tableAlias;
	
	public String getNativeColumnName() {
		return nativeColumnName;
	}

	public void setNativeColumnName(String nativeColumnName) {
		this.nativeColumnName = nativeColumnName;
	}
	
	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}
	
}

