package com.bstek.dorado.idesupport.model;

public class Reference {
	private Rule rule;
	private String property;

	public Reference(Rule rule, String property) {
		this.rule = rule;
		this.property = property;
	}

	public Rule getRule() {
		return rule;
	}

	public String getProperty() {
		return property;
	}
}
