package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.jdbc.model.Column;

@XmlNode(
	nodeName="Column", 
	definitionType="com.bstek.dorado.jdbc.model.table.TableColumnDefinition"
)
public class TableColumn extends Column {

	private boolean insertable = true;
	
	private boolean updatable = true;
	
	private Object insertDefaultValue;
	
	private Object updateDefaultValue;

	public boolean isInsertable() {
		return insertable;
	}

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
