package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(implTypes = {"com.bstek.dorado.jdbc.model.autotable.*"})
public abstract class AbstractMatchRule {
	private boolean available = true;
	private boolean not = false;
	
	@ClientProperty(escapeValue = "true")
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}

	@ClientProperty(escapeValue = "false")
	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}
	
}
