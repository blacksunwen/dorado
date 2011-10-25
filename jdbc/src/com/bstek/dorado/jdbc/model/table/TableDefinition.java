package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.data.variant.VariantUtils;
import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.model.DbElementDefinition;

public class TableDefinition extends DbElementDefinition {

	public TableDefinition() {
		super();
		this.setDefaultImpl(Table.class.getName());
	}
	
	private Boolean autoCreateColumns = null;

	public boolean isAutoCreateColumns() {
		if (autoCreateColumns == null) {
			Object value = this.getProperties().get(JdbcConstants.AUTO_CREATE_COLUMNS);
			this.getProperties().remove(JdbcConstants.AUTO_CREATE_COLUMNS);
			
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

	public void setAppendUnknownColumnOnStartup(boolean appendUnknownColumnOnStartup) {
		this.autoCreateColumns = appendUnknownColumnOnStartup;
	}
	
	public String getCatalog() {
		return (String)this.getProperties().get("catalog");
	}
	
	public String getSchema() {
		return (String)this.getProperties().get("schema");
	}
	
	public String getTableName() {
		return (String)this.getProperties().get("tableName");
	}
}
