package com.bstek.dorado.vidorsupport.internal;

import com.bstek.dorado.vidorsupport.rule.RuleSet;

public class GlobalRuleSetFactory extends AbstractRuleSetFactory {
	
	private RuleSet singleRS;

	@Override
	protected RuleSet doGet() {
		return singleRS;
	}

	@Override
	protected void doCapture(RuleSet rs) {
		this.singleRS = rs;
	}

}
