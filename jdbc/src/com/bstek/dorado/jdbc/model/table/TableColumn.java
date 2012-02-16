package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;

@XmlNode(
	nodeName="Column", 
	definitionType="com.bstek.dorado.jdbc.model.table.TableColumnDefinition"
)
public class TableColumn extends AbstractTableColumn {

	private boolean insertable = true;
	
	private boolean updatable = true;
	
	private Object insertDefaultValue;
	
	private Object updateDefaultValue;

	@ClientProperty(escapeValue = "true")
	public boolean isInsertable() {
		return insertable;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isUpdatable() {
		return updatable;
	}

	@XmlProperty
	public Object getInsertDefaultValue() {
		return insertDefaultValue;
	}

	@XmlProperty
	public Object getUpdateDefaultValue() {
		return updateDefaultValue;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public void setInsertDefaultValue(Object insertDefaultValue) {
		this.insertDefaultValue = insertDefaultValue;
	}

	public void setUpdateDefaultValue(Object updateDefaultValue) {
		this.updateDefaultValue = updateDefaultValue;
	}

}
