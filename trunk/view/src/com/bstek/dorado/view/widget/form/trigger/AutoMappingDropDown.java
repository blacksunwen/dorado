package com.bstek.dorado.view.widget.form.trigger;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "AutoMappingDropDown", category = "Trigger",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.AutoMappingDropDown",
		shortTypeName = "AutoMappingDropDown")
public class AutoMappingDropDown extends RowListDropDown {
	private boolean useEmptyItem;
	private String property = "value";
	private boolean autoOpen;

	public AutoMappingDropDown() {
		setEditable(false);
	}

	public boolean isUseEmptyItem() {
		return useEmptyItem;
	}

	public void setUseEmptyItem(boolean useEmptyItem) {
		this.useEmptyItem = useEmptyItem;
	}

	@Override
	@ClientProperty(escapeValue = "value")
	public String getProperty() {
		return property;
	}

	@Override
	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public boolean isAutoOpen() {
		return autoOpen;
	}

	@Override
	public void setAutoOpen(boolean autoOpen) {
		this.autoOpen = autoOpen;
	}

	@ClientProperty(escapeValue = "false")
	public boolean isEditable() {
		return super.isEditable();
	}
}
