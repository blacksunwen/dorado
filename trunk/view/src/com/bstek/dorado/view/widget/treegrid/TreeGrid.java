package com.bstek.dorado.view.widget.treegrid;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.tree.Node;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "TreeGrid", category = "Collection",
		dependsPackage = "tree-grid")
@ClientObject(prototype = "dorado.widget.TreeGrid", shortTypeName = "TreeGrid")
public class TreeGrid extends AbstractTreeGrid {

	private List<Node> nodes;

	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Nodes"))
	@ClientProperty
	@IdeProperty(highlight = 1)
	public List<Node> getNodes() {
		if (nodes == null)
			nodes = new ArrayList<Node>();
		return nodes;
	}

	public void addNode(Node node) {
		getNodes().add(node);
	}

}
