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
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.ClientType;
import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.core.pkgs.PackageManager;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.model.ClientEvent;
import com.bstek.dorado.idesupport.model.CompositeType;
import com.bstek.dorado.idesupport.template.ChildTemplate;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.ReferenceTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.util.clazz.ClassUtils;
import com.bstek.dorado.view.output.JsonBuilder;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2015-01-30
 */
public class RuleSetJsonOutputter {
	public void output(Writer writer, RuleTemplateManager ruleTemplateManager)
			throws Exception {
		JsonBuilder jsonBuilder = new JsonBuilder(writer);

		jsonBuilder.object();
		jsonBuilder.key("version").value(ruleTemplateManager.getVersion());

		OutputContext context = new OutputContext();
		outputPackageInfos(jsonBuilder, ruleTemplateManager, context);

		jsonBuilder.key("rules").array();
		for (RuleTemplate ruleTemplate : ruleTemplateManager.getRuleTemplates()) {
			outputRuleTemplate(jsonBuilder, ruleTemplate, context);
		}
		jsonBuilder.endArray();

		jsonBuilder.endObject();
		writer.flush();
	}

	protected void outputPackageInfos(JsonBuilder jsonBuilder,
			RuleTemplateManager ruleTemplateManager, OutputContext context)
			throws Exception {
		Map<String, PackageInfo> finalPackageInfos = new LinkedHashMap<String, PackageInfo>(
				PackageManager.getPackageInfoMap());

		Collection<PackageInfo> packageInfos = ruleTemplateManager
				.getPackageInfos();
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

	protected void outputRuleTemplate(JsonBuilder jsonBuilder,
			RuleTemplate ruleTemplate, OutputContext context) throws Exception {
		jsonBuilder.object();
		outputKey(jsonBuilder, ruleTemplate, "name");

		RuleTemplate[] parents = ruleTemplate.getParents();
		if (parents != null && parents.length > 0) {
			String parentsText = "";
			for (RuleTemplate parent : parents) {
				if (parentsText.length() > 0)
					parentsText += ',';
				parentsText += parent.getName();
			}
			jsonBuilder.key("parents").value(parentsText);
		}

		outputKeys(
				jsonBuilder,
				ruleTemplate,
				"label,abstract,nodeName,type,scope,sortFactor,category,robots,icon,labelProperty,autoGenerateId,clientTypes,deprecated,reserve");
		if (!ruleTemplate.isVisible()) {
			jsonBuilder.key("visible").value(false);
		}

		Collection<PropertyTemplate> primitiveProperties = ruleTemplate
				.getPrimitiveProperties().values();
		if (!primitiveProperties.isEmpty()) {
			jsonBuilder.key("primitiveProps").array();
			for (PropertyTemplate property : primitiveProperties) {
				outputProperty(jsonBuilder, property, context);
			}
			jsonBuilder.endArray();
		}

		Collection<PropertyTemplate> properties = ruleTemplate.getProperties()
				.values();
		if (!properties.isEmpty()) {
			jsonBuilder.key("props").array();
			for (PropertyTemplate property : primitiveProperties) {
				outputProperty(jsonBuilder, property, context);
			}
			jsonBuilder.endArray();
		}

		Collection<ClientEvent> clientEvents = ruleTemplate.getClientEvents()
				.values();
		if (!clientEvents.isEmpty()) {
			jsonBuilder.key("clientEvents").array();
			for (ClientEvent clientEvent : clientEvents) {
				outputClientEvent(jsonBuilder, clientEvent, context);
			}
			jsonBuilder.endArray();
		}
		Collection<ChildTemplate> children = ruleTemplate.getChildren()
				.values();
		if (!children.isEmpty()) {
			jsonBuilder.key("children").array();
			for (ChildTemplate childTemplate : children) {
				outputChildTemplate(jsonBuilder, childTemplate, context);
			}
			jsonBuilder.endArray();
		}

		jsonBuilder.endObject();
	}

	protected void outputProperty(JsonBuilder jsonBuilder,
			PropertyTemplate property, OutputContext context) throws Exception {
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

		ReferenceTemplate reference = property.getReference();
		if (reference != null) {
			String referenceText = reference.getRuleTemplate().getName();
			if (StringUtils.isNotEmpty(reference.getProperty())) {
				referenceText += ':' + reference.getProperty();
			}
			jsonBuilder.key("reference").value(referenceText);
		}

		if (compositeType == CompositeType.Fixed
				|| compositeType == CompositeType.Open) {
			jsonBuilder.escapeableKey("props").escapeableArray();
			for (PropertyTemplate subProperty : property.getProperties()
					.values()) {
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
				"name,parameters,clientTypes,deprecated,reserve");
		jsonBuilder.endObject();
	}

	protected void outputChildTemplate(JsonBuilder jsonBuilder,
			ChildTemplate childTemplate, OutputContext context)
			throws Exception {
		jsonBuilder.object();

		RuleTemplate ruleTemplate = childTemplate.getRuleTemplate();
		String ruleName = ruleTemplate.getName();
		if (ruleTemplate.isGlobal()) {
			jsonBuilder.key("rule").value(ruleName);
		}

		outputKeys(jsonBuilder, childTemplate,
				"name,fixed,aggregated,clientTypes,deprecated,reserve");
		if (!childTemplate.isPublic()) {
			jsonBuilder.key("public").value(false);
		}
		if (!childTemplate.isVisible()) {
			jsonBuilder.key("visible").value(false);
		}

		if (!ruleTemplate.isGlobal()) {
			jsonBuilder.key("rule");
			outputRuleTemplate(jsonBuilder, ruleTemplate, context);
		}

		jsonBuilder.endObject();
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
		if (value == null) {
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

		jsonBuilder.escapeableKey(propertyName).value(value);
	}
}
