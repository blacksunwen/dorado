package com.bstek.dorado.view.widget.form.trigger;

import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "ListDropDown", category = "Trigger",
		dependsPackage = "base-widget,list")
@ClientObject(prototype = "dorado.widget.ListDropDown",
		shortTypeName = "ListDropDown")
public class ListDropDown extends RowListDropDown {
	private List<?> items;

	@XmlProperty
	@ClientProperty
	@IdeProperty(editor = "collection[any]", highlight = 1)
	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
}
