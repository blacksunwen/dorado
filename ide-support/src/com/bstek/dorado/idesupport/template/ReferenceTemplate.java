package com.bstek.dorado.idesupport.template;

public abstract class ReferenceTemplate {

	protected String property;

	public ReferenceTemplate(String property) {
		this.property = property;
	}

	public abstract RuleTemplate getRuleTemplate();

	public String getProperty() {
		return property;
	}

}