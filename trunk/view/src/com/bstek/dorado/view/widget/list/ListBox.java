package com.bstek.dorado.view.widget.list;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-28
 */
@Widget(name = "ListBox", category = "Collection", dependsPackage = "list")
@ViewObject(prototype = "dorado.widget.ListBox", shortTypeName = "ListBox")
@XmlNode(nodeName = "ListBox")
public class ListBox extends AbstractListBox {
	private List<?> items;

	@XmlProperty(parser = "dorado.itemsParser")
	@ViewAttribute(outputter = "dorado.dataPropertyOutputter", editor = "collection[pojo]")
	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
}
