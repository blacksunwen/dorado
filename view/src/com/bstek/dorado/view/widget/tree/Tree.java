package com.bstek.dorado.view.widget.tree;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-30
 */
@Widget(name = "Tree", category = "Collection", dependsPackage = "tree")
@ClientObject(prototype = "dorado.widget.Tree", shortTypeName = "Tree")
public class Tree extends AbstractTree implements NodeHolder {
	private List<Node> nodes;

	@XmlSubNode
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
