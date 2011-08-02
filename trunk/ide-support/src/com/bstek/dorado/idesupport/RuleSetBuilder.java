package com.bstek.dorado.idesupport;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.InputStreamResource;
import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.idesupport.model.Child;
import com.bstek.dorado.idesupport.model.ClientEvent;
import com.bstek.dorado.idesupport.model.CompositeType;
import com.bstek.dorado.idesupport.model.Property;
import com.bstek.dorado.idesupport.model.Reference;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.idesupport.parse.ConfigRuleParseContext;
import com.bstek.dorado.idesupport.template.ChildTemplate;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.ReferenceTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-22
 */
public class RuleSetBuilder {
	private XmlDocumentBuilder xmlDocumentBuilder;
	private XmlParser preloadParser;
	private XmlParser ruleTemplateParser;

	public void setXmlDocumentBuilder(XmlDocumentBuilder xmlDocumentBuilder) {
		this.xmlDocumentBuilder = xmlDocumentBuilder;
	}

	public void setPreloadParser(XmlParser preloadParser) {
		this.preloadParser = preloadParser;
	}

	public void setRuleTemplateParser(XmlParser ruleTemplateParser) {
		this.ruleTemplateParser = ruleTemplateParser;
	}

	public RuleSet buildRuleSet(InputStream in) throws Exception {
		Document document = xmlDocumentBuilder
				.loadDocument(new InputStreamResource(in));

		ConfigRuleParseContext parserContext = new ConfigRuleParseContext();
		RuleTemplateManager ruleTemplateManager = (RuleTemplateManager) Context
				.getCurrent().getServiceBean("idesupport.ruleTemplateManager");
		parserContext.setRuleTemplateManager(ruleTemplateManager);

		preloadParser.parse(document.getDocumentElement(), parserContext);
		Map<String, Element> ruleElementMap = parserContext.getRuleElementMap();
		for (String name : ruleElementMap.keySet().toArray(new String[0])) {
			Element element = ruleElementMap.get(name);
			ruleTemplateParser.parse(element, parserContext);
		}

		for (RuleTemplate ruleTemplate : parserContext.getRuleTemplateMap()
				.values()) {
			ruleTemplateManager.addRuleTemplate(ruleTemplate);
		}

		return buildRuleSet(ruleTemplateManager);
	}

	public RuleSet buildRuleSet(RuleTemplateManager ruleTemplateManager)
			throws Exception {
		RuleSet ruleSet = new RuleSet();

		List<PackageInfo> packageInfos = ruleTemplateManager.getPackageInfos();
		if (packageInfos != null) {
			ruleSet.getPackageInfos().addAll(packageInfos);
		}

		for (RuleTemplate ruleTemplate : ruleTemplateManager.getRuleTemplates()) {
			if (ruleSet.getRule(ruleTemplate.getName()) == null) {
				exportRule(ruleTemplate, ruleSet);
			}
		}
		return ruleSet;
	}

	protected Rule exportRule(RuleTemplate ruleTemplate, RuleSet ruleSet)
			throws Exception {
		Rule rule;
		String name = ruleTemplate.getName();
		if (StringUtils.isNotEmpty(name)) {
			rule = ruleSet.getRule(name);
			if (rule != null)
				return rule;
		}

		rule = new Rule(name);
		if (ruleTemplate.isGlobal())
			ruleSet.addRule(rule);

		RuleTemplate[] parentTemplates = ruleTemplate.getParents();
		if (parentTemplates != null && parentTemplates.length > 0) {
			Rule[] parents = new Rule[parentTemplates.length];
			for (int i = 0; i < parentTemplates.length; i++) {
				RuleTemplate parentTemplate = parentTemplates[i];
				parents[i] = exportRule(parentTemplate, ruleSet);
			}
			rule.setParents(parents);
		}
		ruleTemplate.processInheritance();

		applyTemplateToRule(ruleTemplate, rule, ruleSet);
		return rule;
	}

	protected void applyTemplateToRule(RuleTemplate ruleTemplate, Rule rule,
			RuleSet ruleSet) throws Exception {
		applyProperties(ruleTemplate, rule,
				"abstract,nodeName,supportsCustomProperty,category,robots,sortFactor,reserve");

		Map<String, PropertyTemplate> primitiveProperties = ruleTemplate
				.getFinalPrimitiveProperties();
		if (!primitiveProperties.isEmpty()) {
			for (PropertyTemplate propertyTemplate : primitiveProperties
					.values()) {
				if (propertyTemplate.isIgnored()) {
					continue;
				}
				Property property = new Property();
				applyTemplateToProperty(propertyTemplate, property, ruleSet);
				rule.addPrimitiveProperty(property);
			}
		}

		Map<String, PropertyTemplate> properties = ruleTemplate
				.getFinalProperties();
		if (!properties.isEmpty()) {
			for (PropertyTemplate propertyTemplate : properties.values()) {
				if (propertyTemplate.isIgnored()) {
					continue;
				}
				Property property = new Property();
				applyTemplateToProperty(propertyTemplate, property, ruleSet);
				rule.addProperty(property);
			}
		}

		Map<String, ClientEvent> clientEvents = ruleTemplate
				.getFinalClientEvents();
		if (!clientEvents.isEmpty()) {
			rule.addClientEvents(clientEvents.values());
		}

		Collection<ChildTemplate> children = ruleTemplate.getFinalChildren()
				.values();
		if (!children.isEmpty()) {
			for (ChildTemplate childTemplate : children) {
				if (childTemplate.isIgnored()) {
					continue;
				}
				addChildrenByChildTemplate(rule, childTemplate, ruleSet);
			}
		}
	}

	protected void applyTemplateToProperty(PropertyTemplate propertyTemplate,
			Property property, RuleSet ruleSet) throws Exception {
		applyProperties(
				propertyTemplate,
				property,
				"name,type,defaultValue,fixed,enumValues,editor,highlight,visible,compositeType,reserve");

		ReferenceTemplate referenceTemplate = propertyTemplate.getReference();
		if (referenceTemplate != null) {
			Rule rule = exportRule(referenceTemplate.getRuleTemplate(), ruleSet);
			property.setReference(new Reference(rule, referenceTemplate
					.getProperty()));
		}

		if (property.getCompositeType() == CompositeType.Fixed
				|| property.getCompositeType() == CompositeType.Open) {
			Map<String, PropertyTemplate> properties = propertyTemplate
					.getProperties();
			for (Map.Entry<String, PropertyTemplate> entry : properties
					.entrySet()) {
				PropertyTemplate subPopertyTemplate = entry.getValue();
				if (subPopertyTemplate.isIgnored()) {
					continue;
				}

				Property subPoperty = new Property();
				applyTemplateToProperty(subPopertyTemplate, subPoperty, ruleSet);
				property.addProperty(subPoperty);
			}
		}
	}

	private void findConcreteRules(RuleTemplate ruleTemplate,
			Set<Rule> concreteRules, RuleSet ruleSet) throws Exception {
		if (!ruleTemplate.isAbstract()) {
			concreteRules.add(exportRule(ruleTemplate, ruleSet));
		}

		RuleTemplate[] subRuleTemplates = ruleTemplate.getSubRuleTemplates();
		if (subRuleTemplates != null && subRuleTemplates.length > 0) {
			for (RuleTemplate subRuleTemplate : subRuleTemplates) {
				String scope = subRuleTemplate.getScope();
				if ("private".equals(scope)) {
					continue;
				}
				if ("protected".equals(scope) && !subRuleTemplate.isAbstract()) {
					continue;
				}
				findConcreteRules(subRuleTemplate, concreteRules, ruleSet);
			}
		}
	}

	protected void addChildrenByChildTemplate(Rule rule,
			ChildTemplate childTemplate, RuleSet ruleSet) throws Exception {
		Child child = new Child(childTemplate.getName());
		applyTemplateToChild(childTemplate, child, ruleSet);
		rule.addChild(child);

		Set<Rule> concreteRules = child.getConcreteRules();
		findConcreteRules(childTemplate.getRuleTemplate(), concreteRules,
				ruleSet);
	}

	protected void applyTemplateToChild(ChildTemplate childTemplate,
			Child child, RuleSet ruleSet) throws Exception {
		Rule rule = exportRule(childTemplate.getRuleTemplate(), ruleSet);
		child.setRule(rule);
		applyProperties(childTemplate, child, "fixed,aggregated,reserve");
	}

	private void applyProperties(Object source, Object target,
			String propertyNames) throws Exception {
		if (StringUtils.isNotEmpty(propertyNames)) {
			for (String propertyName : StringUtils.split(propertyNames, ',')) {
				applyProperty(source, target, propertyName);
			}
		}
	}

	private void applyProperty(Object source, Object target, String propertyName)
			throws Exception {
		Object value = PropertyUtils.getProperty(source, propertyName);
		if (value != null) {
			PropertyUtils.setProperty(target, propertyName, value);
		}
	}

}
