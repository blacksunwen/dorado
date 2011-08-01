package com.bstek.dorado.view.widget.blockview;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-2-11
 */
@Widget(name = "BlockView", category = "Collection", dependsPackage = "block-view")
@ViewObject(prototype = "dorado.widget.BlockView", shortTypeName = "BlockView")
@XmlNode(nodeName = "BlockView")
public class BlockView extends AbstractBlockView {
	private List<?> items;

	@ViewAttribute(outputter = "dorado.dataPropertyOutputter", editor = "collection[any]")
	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
}
