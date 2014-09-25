package com.bstek.dorado.vidorsupport.iapi;

import com.bstek.dorado.vidorsupport.internal.rule.RuleSet;

public interface IRuleSetFactory {

	/**
	 * 获取一个RuleSet
	 * @return
	 */
	RuleSet get() throws Exception;
	
}
