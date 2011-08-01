package com.bstek.dorado.view.widget.form.trigger;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "AutoMappingDropDown", category = "Trigger", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.AutoMappingDropDown", shortTypeName = "AutoMappingDropDown")
@XmlNode(nodeName = "AutoMappingDropDown")
public class AutoMappingDropDown extends RowListDropDown {
	private boolean useEmptyItem;
	private String property = "value";
	private boolean autoOpen;

	public boolean isUseEmptyItem() {
		return useEmptyItem;
	}

	public void setUseEmptyItem(boolean useEmptyItem) {
		this.useEmptyItem = useEmptyItem;
	}

	@Override
	@ViewAttribute(defaultValue = "value")
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
}
