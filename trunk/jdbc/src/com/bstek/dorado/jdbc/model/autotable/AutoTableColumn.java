package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.model.AbstractUpdatableColumn;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	nodeName="Column",
	definitionType = "com.bstek.dorado.jdbc.config.ColumnDefinition"
)
public class AutoTableColumn extends AbstractUpdatableColumn {

	private String fromTable;

	public String getFromTable() {
		return fromTable;
	}

	public void setFromTable(String fromTable) {
		this.fromTable = fromTable;
	}
	
}

