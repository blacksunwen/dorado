package com.bstek.dorado.view.widget.datacontrol;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-22
 */
public abstract class AbstractPropertyDataControl extends AbstractDataControl
		implements PropertyDataControl {

	private String property;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
