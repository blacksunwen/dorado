/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.idesupport.resolver;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bstek.dorado.idesupport.RuleTemplateBuilder;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.output.RuleSetJsonOutputter;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;
import com.bstek.dorado.web.resolver.HttpConstants;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2015-01-30
 */
public class RuleSetJsonResolver extends AbstractTextualResolver {
	private RuleTemplateBuilder ruleTemplateBuilder;
	private RuleSetJsonOutputter ruleSetOutputter;

	public RuleSetJsonResolver() {
		setContentType(HttpConstants.CONTENT_TYPE_JAVASCRIPT);
		setCacheControl(HttpConstants.NO_CACHE);
	}

	public void setRuleTemplateBuilder(RuleTemplateBuilder ruleTemplateBuilder) {
		this.ruleTemplateBuilder = ruleTemplateBuilder;
	}

	public void setRuleSetOutputter(RuleSetJsonOutputter ruleSetOutputter) {
		this.ruleSetOutputter = ruleSetOutputter;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RuleTemplateManager ruleTemplateManager = ruleTemplateBuilder
				.getRuleTemplateManager();
		PrintWriter writer = getWriter(request, response);
		try {
			ruleSetOutputter.output(writer, ruleTemplateManager);
		} finally {
			writer.flush();
			writer.close();
		}
	}
}
