package com.bstek.dorado.view.widget.form.trigger;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "ListDropDown", category = "Trigger", dependsPackage = "base-widget,list")
@ViewObject(prototype = "dorado.widget.ListDropDown", shortTypeName = "ListDropDown")
@XmlNode(nodeName = "ListDropDown")
public class ListDropDown extends RowListDropDown {
	private List<?> items;

	@XmlProperty(parser = "dorado.itemsParser")
	@ViewAttribute(outputter = "dorado.dataPropertyOutputter", editor = "collection[any]")
	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
}
