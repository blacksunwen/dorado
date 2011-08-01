package com.bstek.dorado.view.widget.tree;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-30
 */
@Widget(name = "Tree", category = "Collection", dependsPackage = "tree")
@ViewObject(prototype = "dorado.widget.Tree", shortTypeName = "Tree")
@XmlNode(nodeName = "Tree")
public class Tree extends AbstractTree implements NodeHolder {
	private List<Node> nodes;

	@ViewAttribute
	@XmlSubNode(path = "#self/*")
	public List<Node> getNodes() {
		if (nodes == null)
			nodes = new ArrayList<Node>();
		return nodes;
	}

	public void addNode(Node node) {
		getNodes().add(node);
	}
}
