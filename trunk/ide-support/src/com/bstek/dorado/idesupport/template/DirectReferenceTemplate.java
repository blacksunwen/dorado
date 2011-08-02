package com.bstek.dorado.idesupport.template;

public class DirectReferenceTemplate extends ReferenceTemplate {
	RuleTemplate ruleTemplate;

	public DirectReferenceTemplate(RuleTemplate ruleTemplate, String property) {
		super(property);
		this.ruleTemplate = ruleTemplate;
	}

	@Override
	public RuleTemplate getRuleTemplate() {
		return ruleTemplate;
	}
}
