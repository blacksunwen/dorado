package com.bstek.dorado.idesupport.template;

import org.apache.commons.lang.ObjectUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-25
 */
public class AutoRuleTemplate extends RuleTemplate {

	public AutoRuleTemplate(String name) {
		super(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj instanceof AutoRuleTemplate) {
			AutoRuleTemplate art = (AutoRuleTemplate) obj;
			return ObjectUtils.equals(getNodeName(), art.getNodeName())
					&& ObjectUtils.equals(getParser(), art.getParser());
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(getNodeName()) * 37
				+ ObjectUtils.hashCode(getParser());
	}

}
