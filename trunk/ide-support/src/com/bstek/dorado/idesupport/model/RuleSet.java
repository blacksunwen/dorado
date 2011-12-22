package com.bstek.dorado.idesupport.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-22
 */
public class RuleSet {
	private String version;
	private List<PackageInfo> packageInfos = new ArrayList<PackageInfo>();
	private Map<String, Rule> ruleMap = new HashMap<String, Rule>();

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<PackageInfo> getPackageInfos() {
		return packageInfos;
	}

	public void addRule(Rule rule) {
		Assert.notEmpty(rule.getName());
		ruleMap.put(rule.getName(), rule);
	}

	public Rule getRule(String name) {
		return ruleMap.get(name);
	}

	public Map<String, Rule> getRuleMap() {
		return ruleMap;
	}
}
