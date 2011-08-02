package com.bstek.dorado.idesupport.template;

import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-19
 */
public class ChildTemplate {
	private String name;
	private RuleTemplate ruleTemplate;
	private boolean fixed;
	private boolean aggregated;
	private boolean ignored;
	private String reserve;

	public ChildTemplate(String name) {
		this.name = name;
		Assert.notEmpty(name);
	}

	public ChildTemplate(String name, RuleTemplate ruleTemplate) {
		this(name);
		this.ruleTemplate = ruleTemplate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RuleTemplate getRuleTemplate() {
		return ruleTemplate;
	}

	public void setRuleTemplate(RuleTemplate ruleTemplate) {
		this.ruleTemplate = ruleTemplate;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public boolean isAggregated() {
		return aggregated;
	}

	public void setAggregated(boolean aggregated) {
		this.aggregated = aggregated;
	}

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
}
