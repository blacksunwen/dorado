package com.bstek.dorado.vidorsupport.internal;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.idesupport.RuleSetBuilder;
import com.bstek.dorado.idesupport.RuleTemplateBuilder;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.vidorsupport.iapi.IRuleSetFactory;
import com.bstek.dorado.vidorsupport.rule.RuleSet;

public abstract class AbstractRuleSetFactory implements IRuleSetFactory {

	public RuleSet get()  throws Exception {
		RuleSet rs = this.doGet();
		if (rs == null) {
			synchronized (this.getClass()) {
				rs = this.doGet(); 
				if (rs == null) {
					rs = this.doCreate();
					this.doCapture(rs);
				}
			}
		}
		return rs;
	}
	
	protected abstract RuleSet doGet();
	protected abstract void doCapture(RuleSet rs);

	protected RuleSet doCreate() throws Exception {
		com.bstek.dorado.idesupport.model.RuleSet ruleSet = this.buildDoradoRuleSet();
		RuleSet rs = new com.bstek.dorado.vidorsupport.rule.RuleSet(ruleSet);
		return rs;
	}

	private com.bstek.dorado.idesupport.model.RuleSet buildDoradoRuleSet() throws Exception {
		Context context = Context.getCurrent();
		RuleTemplateBuilder ruleTemplateBuilder = (RuleTemplateBuilder) context
				.getServiceBean("idesupport.ruleTemplateBuilder");
		RuleSetBuilder ruleSetBuilder = (RuleSetBuilder) context
				.getServiceBean("idesupport.ruleSetBuilder");

		RuleTemplateManager ruleTemplateManager = ruleTemplateBuilder
				.getRuleTemplateManager();
		com.bstek.dorado.idesupport.model.RuleSet ruleSet = ruleSetBuilder.buildRuleSet(ruleTemplateManager);
		return ruleSet;
	}
}
