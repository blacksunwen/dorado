/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.idesupport.parse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ConfigurableDispatchableXmlParser;
import com.bstek.dorado.idesupport.model.ClientEvent;
import com.bstek.dorado.idesupport.template.ChildTemplate;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-18
 */
public class RuleTemplateParser extends ConfigurableDispatchableXmlParser {

	public RuleTemplate getRuleTemplate(String name,
			ConfigRuleParseContext parserContext) throws Exception {
		RuleTemplate ruleTemplate = parserContext.getRuleTemplateMap()
				.get(name);
		if (ruleTemplate == null) {
			Element element = parserContext.getRuleElementMap().get(name);
			if (element != null) {
				ruleTemplate = (RuleTemplate) parse(element, parserContext);
			}
		}
		return ruleTemplate;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Element element = (Element) node;
		ConfigRuleParseContext parserContext = (ConfigRuleParseContext) context;
		RuleTemplate ruleTemplate;

		String name = element.getAttribute("name");
		if (StringUtils.isNotEmpty(name)) {
			ruleTemplate = parserContext.getRuleTemplateMap().get(name);
			if (ruleTemplate != null) {
				return ruleTemplate;
			}
		}

		ruleTemplate = new RuleTemplate(name);
		if (StringUtils.isNotBlank(name)
				&& parserContext.getRuleElementMap().containsKey(name)) {
			parserContext.getRuleTemplateMap().put(name, ruleTemplate);
			ruleTemplate.setGlobal(true);
		}

		Map<String, Object> properties = parseProperties(element, context);

		String[] parents = (String[]) properties.remove("parents");
		if (parents != null) {
			List<RuleTemplate> parentList = new ArrayList<RuleTemplate>();
			for (String parentName : parents) {
				RuleTemplate parent = getRuleTemplate(parentName, parserContext);
				if (parent != null)
					parentList.add(parent);
			}
			properties.put("parents", parentList.toArray(new RuleTemplate[0]));
		}
		BeanUtils.copyProperties(ruleTemplate, properties);

		Element primitivePropsEl = DomUtils.getChildByTagName(element,
				"PrimitiveProps");
		if (primitivePropsEl != null) {
			ruleTemplate
					.addPrimitiveProperties((Collection<PropertyTemplate>) dispatchChildElements(
							primitivePropsEl, parserContext));
		}

		Element propsEl = DomUtils.getChildByTagName(element, "Props");
		if (propsEl != null) {
			ruleTemplate
					.addProperties((Collection<PropertyTemplate>) dispatchChildElements(
							propsEl, parserContext));
		}

		Element eventsEl = DomUtils.getChildByTagName(element, "ClientEvents");
		if (eventsEl != null) {
			ruleTemplate
					.addClientEvents((Collection<ClientEvent>) dispatchChildElements(
							eventsEl, parserContext));
		}

		Element childrenEl = DomUtils.getChildByTagName(element, "Children");
		if (childrenEl != null) {
			ruleTemplate
					.addChildren((Collection<ChildTemplate>) dispatchChildElements(
							childrenEl, parserContext));
		}
		return ruleTemplate;
	}
}
