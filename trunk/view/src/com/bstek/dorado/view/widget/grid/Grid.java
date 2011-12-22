package com.bstek.dorado.view.widget.grid;

import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-28
 */
@Widget(name = "Grid", category = "Collection", dependsPackage = "grid")
@ClientObject(prototype = "dorado.widget.Grid", shortTypeName = "Grid")
public class Grid extends AbstractGrid {
	private List<?> items;

	@XmlProperty
	@ClientProperty
	@IdeProperty(editor = "collection[pojo]", highlight = 1)
	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
}
