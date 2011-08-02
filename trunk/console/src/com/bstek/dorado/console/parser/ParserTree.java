package com.bstek.dorado.console.parser;

import java.util.Collection;

public class ParserTree {

	private ModelTree modelTree = new ModelTree();
	private ViewConfigTree viewConfigTree = new ViewConfigTree();
	
	public Collection<Node> getModelNodes() throws Exception {
		return modelTree.getAllTopNodes();
	}
	
	public Collection<Node> getViewConfigNodes() throws Exception {
		return viewConfigTree.getAllTopNodes();
	}
}
