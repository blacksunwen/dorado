package com.bstek.dorado.vidorsupport.resolver;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bstek.dorado.vidorsupport.iapi.IRuleSetOutputter;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;
import com.bstek.dorado.web.resolver.HttpConstants;

public class RuleSetJsonResolver extends AbstractTextualResolver {
	private String ruleSet;
	private IRuleSetOutputter ruleSetOutputter;

	public RuleSetJsonResolver() {
		setContentType(HttpConstants.CONTENT_TYPE_JAVASCRIPT);
		setCacheControl(HttpConstants.NO_CACHE);
	}

	public void setRuleSet(String ruleSet) {
		this.ruleSet = ruleSet;
	}

	public void setRuleSetOutputter(IRuleSetOutputter ruleSetOutputter) {
		this.ruleSetOutputter = ruleSetOutputter;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrintWriter writer = getWriter(request, response);
		if (ruleSet == null) {
			ruleSet = ruleSetOutputter.output();
		}
		try {
			writer.write(ruleSet);
		} finally {
			writer.flush();
			writer.close();
		}
	}
}
