package com.bstek.dorado.view.widget.grid;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-28
 */
@Widget(name = "Grid", category = "Collection", dependsPackage = "grid")
@ViewObject(prototype = "dorado.widget.Grid", shortTypeName = "Grid")
@XmlNode(nodeName = "Grid")
public class Grid extends AbstractGrid {
	private List<?> items;

	@ViewAttribute(outputter = "dorado.dataPropertyOutputter", editor = "collection[pojo]")
	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
}
