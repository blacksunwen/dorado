package com.bstek.dorado.vidorsupport.impl;

import com.bstek.dorado.vidorsupport.iapi.IRuleSetFactory;
import com.bstek.dorado.vidorsupport.iapi.IRuleSetOutputter;
import com.bstek.dorado.vidorsupport.internal.output.OutputContext;
import com.bstek.dorado.vidorsupport.internal.rule.RuleSet;

public abstract class AbstractRuleSetOutputter implements IRuleSetOutputter {

	private IRuleSetFactory factory;
	
	protected IRuleSetFactory getFactory() {
		return factory;
	}
	public void setFactory(IRuleSetFactory factory) {
		this.factory = factory;
	}
	
	protected String doCreate(RuleSet rs) throws Exception {
		OutputContext outputContext = new OutputContext();
		rs.output(outputContext);
		String output = outputContext.getWriter().toString();
		return output;
	}
}
