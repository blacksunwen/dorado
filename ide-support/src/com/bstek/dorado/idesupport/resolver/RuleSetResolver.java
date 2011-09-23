package com.bstek.dorado.idesupport.resolver;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bstek.dorado.idesupport.RuleTemplateBuilder;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.output.RuleSetOutputter;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;
import com.bstek.dorado.web.resolver.HttpConstants;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-18
 */
public class RuleSetResolver extends AbstractTextualResolver {
	public static final String ROBOT_MAP_ATTRIBUTE_KEY = RuleSetResolver.class
			.getName() + ".robotMap";

	private RuleTemplateBuilder ruleTemplateBuilder;
	private RuleSetOutputter ruleSetOutputter;

	public RuleSetResolver() {
		setContentType(HttpConstants.CONTENT_TYPE_XML);
		setCacheControl(HttpConstants.NO_STORE);
	}

	public void setRuleTemplateBuilder(RuleTemplateBuilder ruleTemplateBuilder) {
		this.ruleTemplateBuilder = ruleTemplateBuilder;
	}

	public void setRuleSetOutputter(RuleSetOutputter ruleSetOutputter) {
		this.ruleSetOutputter = ruleSetOutputter;
	}

	@Override
	protected void execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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
