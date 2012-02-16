package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlProperty;

public abstract class AbstractUpdatableColumn extends AbstractColumn {
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
