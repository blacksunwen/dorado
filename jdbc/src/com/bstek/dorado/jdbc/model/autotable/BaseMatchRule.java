package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.ClientProperty;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class BaseMatchRule extends AbstractMatchRule {
	private boolean not = false;

	@ClientProperty(escapeValue = "false")
	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}

}
