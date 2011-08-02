package com.bstek.dorado.idesupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.ClassUtils;

import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-18
 */
public class RuleTemplateManager {
	private List<PackageInfo> packageInfos = new ArrayList<PackageInfo>();
	private Map<String, RuleTemplate> ruleTemplateMap = new LinkedHashMap<String, RuleTemplate>();
	private List<RuleTemplateManagerListener> listeners;

	public List<PackageInfo> getPackageInfos() {
		return packageInfos;
	}

	public void addRuleTemplate(RuleTemplate ruleTemplate) throws Exception {
		ruleTemplate.setGlobal(true);
		if (listeners != null) {
			for (RuleTemplateManagerListener listener : listeners) {
				listener.ruleTemplateAdded(this, ruleTemplate);
			}
		}
		String name = ruleTemplate.getName();
		Assert.notEmpty(name);
		ruleTemplateMap.put(name, ruleTemplate);
	}

	public void removeRuleTemplate(String name) throws Exception {
		ruleTemplateMap.remove(name);
	}

	public Collection<RuleTemplate> getRuleTemplates() {
		return ruleTemplateMap.values();
	}

	public RuleTemplate getRuleTemplate(String ruleName) {
		return ruleTemplateMap.get(ruleName);
	}

	public RuleTemplate getRuleTemplate(Class<?> type)
			throws ClassNotFoundException {
		for (RuleTemplate ruleTemplate : ruleTemplateMap.values()) {
			if (ruleTemplate.getType() != null) {
				Class<?> ruleType = ClassUtils.getClass(ruleTemplate.getType());
				if (ruleType.equals(type)) {
					return ruleTemplate;
				}
			}
		}
		return null;
	}

	public void addListener(RuleTemplateManagerListener listener) {
		if (listeners == null) {
			listeners = new Vector<RuleTemplateManagerListener>();
		}
		listeners.add(listener);
	}

	public void removeListener(RuleTemplateManagerListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}
}
