package com.bstek.dorado.view.widget.datacontrol;

import com.bstek.dorado.annotation.IdeProperty;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-22
 */
public abstract class AbstractPropertyDataControl extends AbstractDataControl
		implements PropertyDataControl {

	private String property;

	@IdeProperty(highlight = 1)
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
