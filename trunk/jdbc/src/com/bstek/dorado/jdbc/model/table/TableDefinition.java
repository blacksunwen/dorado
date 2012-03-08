package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.config.AbstractDbTableDefinition;
import com.bstek.dorado.jdbc.config.XmlConstants;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableDefinition extends AbstractDbTableDefinition {

	private Boolean autoCreateColumns = null;

	public boolean isAutoCreateColumns() {
		if (autoCreateColumns == null) {
			autoCreateColumns = this.getVirtualPropertyBoolean(XmlConstants.AUTO_CREATE_COLUMNS, false);
		}
		return autoCreateColumns;
	}

	public void setAutoCreateColumns(boolean autoCreateColumns) {
		this.autoCreateColumns = autoCreateColumns;
	}
	
	public String getTableName() {
		return (String)this.getProperties().get(XmlConstants.TABLE_NAME);
	}
	
	public String getNamespace() {
		return (String)this.getProperties().get(XmlConstants.NAME_SPACE);
	}
	
}
