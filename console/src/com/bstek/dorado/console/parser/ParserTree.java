package com.bstek.dorado.console.parser;

import java.util.Collection;

public class ParserTree {
	private ModelTree modelTree;
	private ViewConfigTree viewConfigTree;

	public void setModelTree(ModelTree modelTree) {
		this.modelTree = modelTree;
	}

	public void setViewConfigTree(ViewConfigTree viewConfigTree) {
		this.viewConfigTree = viewConfigTree;
	}

	public Collection<Node> getModelNodes() throws Exception {
		return modelTree.getAllTopNodes();
	}

	public Collection<Node> getViewConfigNodes() throws Exception {
		return viewConfigTree.getAllTopNodes();
	}
}
