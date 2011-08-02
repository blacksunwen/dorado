package com.bstek.dorado.idesupport.parse;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.template.RuleTemplate;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-18
 */
public class ConfigRuleParseContext extends ParseContext {
	private Map<String, Element> ruleElementMap = new HashMap<String, Element>();
	private Map<String, RuleTemplate> ruleTemplateMap = new HashMap<String, RuleTemplate>();
	private RuleTemplateManager ruleTemplateManager;

	public Map<String, Element> getRuleElementMap() {
		return ruleElementMap;
	}

	public Map<String, RuleTemplate> getRuleTemplateMap() {
		return ruleTemplateMap;
	}

	public RuleTemplateManager getRuleTemplateManager() {
		return ruleTemplateManager;
	}

	public void setRuleTemplateManager(RuleTemplateManager ruleTemplateManager) {
		this.ruleTemplateManager = ruleTemplateManager;
	}
}
