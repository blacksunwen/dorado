package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.data.variant.VariantUtils;
import com.bstek.dorado.jdbc.config.xml.XmlConstants;
import com.bstek.dorado.jdbc.model.DbElementDefinition;

public class TableDefinition extends DbElementDefinition {

	public TableDefinition() {
		super();
		this.setImpl(Table.class.getName());
	}
	
	private Boolean autoCreateColumns = null;

	public boolean isAutoCreateColumns() {
		if (autoCreateColumns == null) {
			Object value = this.getProperties().get(XmlConstants.AUTO_CREATE_COLUMNS);
			this.getProperties().remove(XmlConstants.AUTO_CREATE_COLUMNS);
			
			try {
				value = this.getFinalValueOrExpression(value, null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			if (value != null) {
				if (value instanceof Expression) {
					throw new IllegalArgumentException("not support EL expression.");
				}
				autoCreateColumns = VariantUtils.toBoolean(value);
			} else {
				autoCreateColumns = false;
			}
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
