package com.bstek.dorado.view.widget.datacontrol;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;

@ClientEvents({ @ClientEvent(name = "onGetBindingData"),
		@ClientEvent(name = "onGetBindingDataType") })
public interface PropertyDataControl extends DataControl {
	/**
	 * @return the property
	 */
	public String getProperty();

	/**
	 * @param property
	 *            the property to set
	 */
	public void setProperty(String property);
}
