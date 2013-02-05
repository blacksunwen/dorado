package com.bstek.dorado.console.web.outputter;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

public abstract class Category {
	private String name;
	private List<Node> nodes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category(String name) {
		this.name = name;
		nodes = initNodes();
	}

	public List<Node> getNodes() {

		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public abstract List<Node> initNodes();
}

class ComparatorNode implements Comparator<Node> {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Node o1, Node o2) {
		Comparator cmp = Collator.getInstance(java.util.Locale.ENGLISH);
		return cmp.compare(o1.getName(), o2.getName());
	}
}
