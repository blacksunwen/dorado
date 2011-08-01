package com.bstek.dorado.view.widget.treegrid;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.tree.Node;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "TreeGrid", category = "Collection", dependsPackage = "tree-grid")
@ViewObject(prototype = "dorado.widget.TreeGrid", shortTypeName = "TreeGrid")
@XmlNode(nodeName = "TreeGrid")
public class TreeGrid extends AbstractTreeGrid {

	private List<Node> nodes;

	@ViewAttribute
	@XmlSubNode(path = "Nodes/*", fixed = true)
	public List<Node> getNodes() {
		if (nodes == null)
			nodes = new ArrayList<Node>();
		return nodes;
	}

	public void addNode(Node node) {
		getNodes().add(node);
	}

}
