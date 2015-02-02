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

package com.bstek.dorado.idesupport.output;

import java.io.Writer;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.ClientType;
import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.core.pkgs.PackageManager;
import com.bstek.dorado.idesupport.model.Child;
import com.bstek.dorado.idesupport.model.ClientEvent;
import com.bstek.dorado.idesupport.model.CompositeType;
import com.bstek.dorado.idesupport.model.Property;
import com.bstek.dorado.idesupport.model.Reference;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.util.clazz.ClassUtils;
import com.bstek.dorado.view.output.ClientObjectOutputter;
import com.bstek.dorado.view.output.ClientOutputHelper;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegistry;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2015-01-30
 */
public class ComputedRuleSetJsonOutputter {
	private ClientOutputHelper clientOutputHelper;
	private ComponentTypeRegistry componentTypeRegistry;

	public void setClientOutputHelper(ClientOutputHelper clientOutputHelper) {
		this.clientOutputHelper = clientOutputHelper;
	}

	public void setComponentTypeRegistry(
			ComponentTypeRegistry componentTypeRegistry) {
		this.componentTypeRegistry = componentTypeRegistry;
	}

	public void output(Writer writer, RuleSet ruleSet) throws Exception {
		JsonBuilder jsonBuilder = new JsonBuilder(writer);
		jsonBuilder.setPrettyFormat(true);

		jsonBuilder.object();
		jsonBuilder.key("version").value(ruleSet.getVersion());

		OutputContext context = new OutputContext();
		outputPackageInfos(jsonBuilder, ruleSet, context);

		jsonBuilder.escapeableKey("rules").escapeableArray();
		for (Rule rule : ruleSet.getRuleMap().values()) {
			if (rule.isAbstract()) {
				continue;
			}
			outputRule(jsonBuilder, rule, context);
		}
		jsonBuilder.endArray();

		jsonBuilder.endObject();
		writer.flush();
	}

	protected void outputPackageInfos(JsonBuilder jsonBuilder, RuleSet ruleSet,
			OutputContext context) throws Exception {
		Map<String, PackageInfo> finalPackageInfos = new LinkedHashMap<String, PackageInfo>(
				PackageManager.getPackageInfoMap());

		Collection<PackageInfo> packageInfos = ruleSet.getPackageInfos();
		if (packageInfos != null) {
			for (PackageInfo packageInfo : packageInfos) {
				finalPackageInfos.put(packageInfo.getName(), packageInfo);
			}
		}

		jsonBuilder.key("packageInfos");
		jsonBuilder.array();
		for (PackageInfo packageInfo : finalPackageInfos.values()) {
			jsonBuilder.object();
			outputKeys(jsonBuilder, packageInfo, "name,version");
			jsonBuilder.endObject();
		}
		jsonBuilder.endArray();
	}

	protected void outputRule(JsonBuilder jsonBuilder, Rule rule,
			OutputContext context) throws Exception {
		jsonBuilder.object();

		if (rule.isGlobal()) {
			outputKey(jsonBuilder, rule, "name");
		}

		outputKeys(
				jsonBuilder,
				rule,
				"label,nodeName,type,sortFactor,category,icon,labelProperty,autoGenerateId,clientTypes,deprecated,reserve");
		if (!rule.isVisible()) {
			jsonBuilder.key("visible").value(false);
		}

		String className = rule.getType();
		if (StringUtils.isNotEmpty(className)) {
			Class<?> type = ClassUtils.forName(className);

			if (Component.class.isAssignableFrom(type)) {
				if (Control.class.isAssignableFrom(type)) {
					jsonBuilder.key("isControl").value(true);
					if (Container.class.isAssignableFrom(type)) {
						jsonBuilder.key("isContainer").value(true);
					}
				}

				ComponentTypeRegisterInfo registerInfo = componentTypeRegistry
						.getRegisterInfo(type);
				if (registerInfo != null
						&& StringUtils.isNotEmpty(registerInfo
								.getDependsPackage())) {
					jsonBuilder.key("dependsPackage").value(
							registerInfo.getDependsPackage());
				}
			}

			Outputter outputter = clientOutputHelper.getOutputter(type);
			if (outputter instanceof ClientObjectOutputter) {
				ClientObjectOutputter coo = (ClientObjectOutputter) outputter;
				if (StringUtils.isNotEmpty(coo.getPrototype())) {
					jsonBuilder.key("jsPrototype").value(coo.getPrototype());
				}
				if (StringUtils.isNotEmpty(coo.getShortTypeName())) {
					jsonBuilder.key("jsShortType")
							.value(coo.getShortTypeName());
				}
			}
		}

		Collection<Property> primitiveProperties = rule
				.getPrimitiveProperties().values();
		if (!primitiveProperties.isEmpty()) {
			jsonBuilder.key("primitiveProps").array();
			for (Property property : primitiveProperties) {
				outputProperty(jsonBuilder, property, context);
			}
			jsonBuilder.endArray();
		}

		Collection<Property> properties = rule.getProperties().values();
		if (!properties.isEmpty()) {
			jsonBuilder.key("props").array();
			for (Property property : properties) {
				outputProperty(jsonBuilder, property, context);
			}
			jsonBuilder.endArray();
		}

		Collection<ClientEvent> clientEvents = rule.getClientEvents().values();
		if (!clientEvents.isEmpty()) {
			jsonBuilder.key("clientEvents").array();
			for (ClientEvent clientEvent : clientEvents) {
				outputClientEvent(jsonBuilder, clientEvent, context);
			}
			jsonBuilder.endArray();
		}

		Collection<Child> children = rule.getChildren().values();
		if (!children.isEmpty()) {
			jsonBuilder.key("children").array();
			for (Child child : children) {
				outputChild(jsonBuilder, child, context);
			}
			jsonBuilder.endArray();
		}

		jsonBuilder.endObject();
	}

	private void doFindAllSubRules(Rule rule, Set<Rule> subRules) {
		if (!rule.isAbstract()) {
			subRules.add(rule);
		}
		for (Rule subRule : rule.getSubRules()) {
			doFindAllSubRules(subRule, subRules);
		}
	}

	private Set<Rule> findAllSubRules(Rule rule) {
		Set<Rule> subRules = new TreeSet<Rule>(new Comparator<Rule>() {
			public int compare(Rule rule1, Rule rule2) {
				int result = rule1.getSortFactor() - rule2.getSortFactor();
				if (result == 0) {
					result = rule1.getName().compareTo(rule2.getName());
				}
				return result;
			}
		});

		doFindAllSubRules(rule, subRules);
		return subRules;
	}

	protected void outputProperty(JsonBuilder jsonBuilder, Property property,
			OutputContext context) throws Exception {
		jsonBuilder.object();
		outputKeys(
				jsonBuilder,
				property,
				"name,defaultValue,highlight,fixed,enumValues,editor,clientTypes,deprecated,reserve");
		if (!property.isVisible()) {
			jsonBuilder.key("visible").value(false);
		}

		if (StringUtils.isNotEmpty(property.getType())) {
			try {
				Class<?> type = ClassUtils.forName(property.getType());
				if (!String.class.equals(type) && !type.isEnum()) {
					jsonBuilder.key("type").value(type.getName());
				}
			} catch (ClassNotFoundException e) {
				// do nothing
			}
		}

		CompositeType compositeType = property.getCompositeType();
		if (compositeType != CompositeType.Unsupport) {
			jsonBuilder.key("compositeType").value(compositeType.toString());
		}

		Reference reference = property.getReference();
		if (reference != null) {
			StringBuffer referenceText = new StringBuffer();
			Rule rule = reference.getRule();
			for (Rule subRule : findAllSubRules(rule)) {
				if (referenceText.length() > 0) {
					referenceText.append(',');
				}
				referenceText.append(subRule.getName());
				if (StringUtils.isNotEmpty(reference.getProperty())) {
					referenceText.append(':').append(reference.getProperty());
				}
			}
			jsonBuilder.key("reference").value(referenceText.toString());
		}

		if ((compositeType == CompositeType.Fixed || compositeType == CompositeType.Open)
				&& !property.getProperties().isEmpty()) {
			jsonBuilder.key("props").array();
			for (Property subProperty : property.getProperties().values()) {
				outputProperty(jsonBuilder, subProperty, context);
			}
			jsonBuilder.endArray();
		}

		jsonBuilder.endObject();
	}

	protected void outputClientEvent(JsonBuilder jsonBuilder,
			ClientEvent clientEvent, OutputContext context) throws Exception {
		jsonBuilder.object();
		outputKeys(jsonBuilder, clientEvent,
				"name,clientTypes,deprecated,reserve");
		String parameters = StringUtils.join(clientEvent.getParameters(), ',');
		if (StringUtils.isNotEmpty(parameters) && !"self,arg".equals(parameters)) {
			jsonBuilder.key("parameters").value(parameters);
		}
		jsonBuilder.endObject();
	}

	protected void outputChild(JsonBuilder jsonBuilder, Child child,
			OutputContext context) throws Exception {
		for (Rule subRule : child.getConcreteRules()) {
			jsonBuilder.object();

			String ruleName = subRule.getName();
			if (subRule.isGlobal()) {
				jsonBuilder.key("rule").value(ruleName);
			}

			outputKeys(jsonBuilder, child,
					"property,fixed,clientTypes,deprecated,reserve");
			if (!child.isAggregated()) {
				jsonBuilder.key("aggregated").value(false);
			}
			if (!subRule.isVisible()) {
				jsonBuilder.key("visible").value(false);
			}

			if (!subRule.isGlobal()) {
				jsonBuilder.key("rule");
				outputRule(jsonBuilder, subRule, context);
			}

			jsonBuilder.endObject();
		}
	}

	private void outputKeys(JsonBuilder jsonBuilder, Object object,
			String propertyNames) throws Exception {
		for (String propertyName : StringUtils.split(propertyNames, ',')) {
			outputKey(jsonBuilder, object, propertyName);
		}
	}

	private void outputKey(JsonBuilder jsonBuilder, Object object,
			String propertyName) throws Exception {
		Object value = PropertyUtils.getProperty(object, propertyName);
		if (value == null
				|| Boolean.FALSE.equals(value)
				|| (value instanceof Number && ((Number) value).intValue() == 0)) {
			return;
		}

		if (propertyName.equals("clientTypes")) {
			value = ClientType.toString(((Integer) value).intValue());
		} else if (value instanceof String[]) {
			value = StringUtils.join((String[]) value, ',');
		} else if (value instanceof Class<?>) {
			if (value != String.class)
				value = ((Class<?>) value).getName();
		}

		if (value instanceof String && StringUtils.isEmpty((String) value)) {
			return;
		}
		jsonBuilder.escapeableKey(propertyName).value(value);
	}
}
