package com.bstek.dorado.idesupport.template;

import com.bstek.dorado.idesupport.RuleTemplateManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-1
 */
public class LazyReferenceTemplate extends ReferenceTemplate {
	private RuleTemplateManager ruleTemplateManager;
	private String ruleName;
	private RuleTemplate ruleTemplate;

	public LazyReferenceTemplate(RuleTemplateManager ruleTemplateManager,
			String ruleName, String property) {
		super(property);
		this.ruleTemplateManager = ruleTemplateManager;
		this.ruleName = ruleName;
	}

	@Override
	public RuleTemplate getRuleTemplate() {
		if (ruleTemplate == null) {
			ruleTemplate = ruleTemplateManager.getRuleTemplate(ruleName);
		}
		return ruleTemplate;
	}

}
