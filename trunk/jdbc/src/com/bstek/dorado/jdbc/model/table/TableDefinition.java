package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.config.xml.XmlConstants;
import com.bstek.dorado.jdbc.model.DbElementDefinition;

public class TableDefinition extends DbElementDefinition {

	private Boolean autoCreateColumns = null;
	private Boolean autoCreateDataType = null;
	private Boolean autoCreateDataProvider = null;

	public boolean isAutoCreateDataProvider() {
		if (autoCreateDataProvider == null) {
			autoCreateDataProvider = this.getFinalPropertyBoolean(XmlConstants.AUTO_CREATE_DATAPROVIDER, false);
		}
		return autoCreateDataProvider;
	}

	public void setAutoCreateDataProvider(boolean autoCreateDataProvider) {
		this.autoCreateDataProvider = autoCreateDataProvider;
	}

	public boolean isAutoCreateDataType() {
		if (autoCreateDataType == null) {
			autoCreateDataType = this.getFinalPropertyBoolean(XmlConstants.AUTO_CREATE_DATATYPE, false);
		}
		return autoCreateDataType;
	}

	public void setAutoCreateDataType(boolean autoCreateDataType) {
		this.autoCreateDataType = autoCreateDataType;
	}

	public boolean isAutoCreateColumns() {
		if (autoCreateColumns == null) {
			autoCreateColumns = this.getFinalPropertyBoolean(XmlConstants.AUTO_CREATE_COLUMNS, false);
		}
		return autoCreateColumns;
	}

	public void setAutoCreateColumns(boolean autoCreateColumns) {
		this.autoCreateColumns = autoCreateColumns;
	}
	
	public String getCatalog() {
		return (String)this.getProperties().get(XmlConstants.CATALOG);
	}
	
	public String getSchema() {
		return (String)this.getProperties().get(XmlConstants.SCHEMA);
	}
	
	public String getTableName() {
		return (String)this.getProperties().get(XmlConstants.TABLE_NAME);
	}
	
}
