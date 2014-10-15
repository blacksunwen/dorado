package com.bstek.dorado.vidorsupport.rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.dom4j.Element;

import com.bstek.dorado.vidorsupport.exception.XmlException;
import com.bstek.dorado.vidorsupport.output.AbstractField;
import com.bstek.dorado.vidorsupport.output.BooleanField;
import com.bstek.dorado.vidorsupport.output.IOutputFiledsable;
import com.bstek.dorado.vidorsupport.output.IOutputable;
import com.bstek.dorado.vidorsupport.output.OutputContext;
import com.bstek.dorado.vidorsupport.output.StringArrayField;
import com.bstek.dorado.vidorsupport.output.StringField;
import com.bstek.dorado.common.ClientType;
import com.bstek.dorado.idesupport.model.Rule;

public class Child implements IOutputable<OutputContext>, IOutputFiledsable, IOriginalObjectHolder<com.bstek.dorado.idesupport.model.Child> {
	/**
	 * "子对象"的名称
	 */
	private StringField name = new StringField("name");
	/**
	 * 所属规则的属性名称
	 */
	private StringField property = new StringField("property");
	/**
	 * 是否与"规则"一同被创建。
	 */
	private BooleanField fixed = new BooleanField("fixed", false);
	/**
	 * "规则"下可以存放的Child的数量，只能存放一个这种Child还是可以存放多个
	 */
	private BooleanField aggregated = new BooleanField("aggregated", true);
	/**
	 * 在xml结构中是否使用包装器
	 */
	private BooleanField wrappered = new BooleanField("wrappered", false);
	/**
	 * 可以存放的成员数量，是只能放一个还是多个
	 */
	private BooleanField memberAggregated = new BooleanField("memberAggregated", true);
	
	/**
	 * 可以存放的成员的"规则"的ID
	 */
	private StringArrayField memberRuleIDs = new StringArrayField("memberRuleIDs");
	
	private com.bstek.dorado.idesupport.model.Child originalObject;
	public Child() {
		super();
	}
	
	public Child(com.bstek.dorado.idesupport.model.Child child) {
		this();
		
		this.setName(child.getName());
		this.setProperty(StringUtils.uncapitalize(this.getName()));
		this.setFixed(child.isFixed());
		this.setAggregated(child.isAggregated());
		Rule childRule = child.getRule();
		this.setWrappered(childRule.getName().startsWith("Wrapper."));
		Collection<com.bstek.dorado.idesupport.model.Child> children = childRule.getChildren().values();
		if (children.size() == 1) {
			com.bstek.dorado.idesupport.model.Child innerChild = children.iterator().next();
			this.setMemberAggregated(innerChild.isAggregated());
		}
	}
	
	public com.bstek.dorado.idesupport.model.Child getOriginalObject() {
		return originalObject;
	}
	
	public AbstractField<?>[] getOutputFilelds() {
		AbstractField<?>[] fields = new AbstractField<?>[]{
			name, property, fixed, aggregated, wrappered, 
			memberAggregated, memberRuleIDs};
		return fields;
	}
	
	public void output(OutputContext context) {
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			jsonGenerator.writeStartObject();
			AbstractField<?>[] fields = this.getOutputFilelds();
			for (AbstractField<?> field: fields) {
				if (field.shouldOutput())
					field.output(context);
			}
			jsonGenerator.writeEndObject();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getName() {
		return name.getValue();
	}
	public void setName(String name) {
		this.name.setValue(name);
	}
	public String getProperty() {
		return property.getValue();
	}
	public void setProperty(String property) {
		this.property.setValue(property);
	}
	public boolean isFixed() {
		return fixed.getValue();
	}
	public void setFixed(boolean fixed) {
		this.fixed.setValue(fixed);
	}
	public boolean isAggregated() {
		return aggregated.getValue();
	}
	public void setAggregated(boolean aggregated) {
		this.aggregated.setValue(aggregated);
	}
	public boolean isWrappered() {
		return wrappered.getValue();
	}
	public void setWrappered(boolean wrappered) {
		this.wrappered.setValue(wrappered);
	}
	public boolean isMemberAggregated() {
		return memberAggregated.getValue();
	}
	public void setMemberAggregated(boolean memberAggregated) {
		this.memberAggregated.setValue(memberAggregated);
	}
	public String[] getMemberRuleIDs() {
		return memberRuleIDs.getValue();
	}
	public void setMemberRuleIDs(String[] memberRuleIDs) {
		this.memberRuleIDs.setValue(memberRuleIDs);
	}
	
	public com.bstek.dorado.vidorsupport.rule.Rule findRule(RuleSet ruleSet, Element element, int clientType) {
		String[] memberRuleIDs = this.getMemberRuleIDs();
		if (memberRuleIDs == null || memberRuleIDs.length == 0) {
			return null;
		}
		
		com.bstek.dorado.vidorsupport.rule.Rule defaultRule = null;
		List<com.bstek.dorado.vidorsupport.rule.Rule> fixedRules = new ArrayList<com.bstek.dorado.vidorsupport.rule.Rule>(5);
		String xmlNodeName = element.getName();
		
		for (String ruleId: memberRuleIDs) {
			com.bstek.dorado.vidorsupport.rule.Rule memberRule = ruleSet.getRule(ruleId);
			String ruleXmlNodeName = memberRule.getXmlNodeName();
			int ruleClientType = memberRule.getClientTypes();
			if (xmlNodeName.equals(ruleXmlNodeName)) {
				if (this.matchClientType(clientType, ruleClientType)) {
					Property[] properties = memberRule.getFixedProperties();
					if (properties.length == 0) {
						if (defaultRule == null) {
							defaultRule = memberRule;
						} else {
							String msg = "more than one rule be found [" + memberRule.getId() + "," + defaultRule.getId() + "].";
							throw new XmlException(element, msg);
						}
					} else {
						fixedRules.add(memberRule);
					}
				}
			}
		}
		
		for (com.bstek.dorado.vidorsupport.rule.Rule rule: fixedRules) {
			boolean match = true;
			Property[] properties = rule.getFixedProperties();
			for (Property property: properties) {
				String propertyName = property.getName();
				String fixedValue = property.getDefaultValue();
				String propertyValue = element.attributeValue(propertyName);
				if (fixedValue != null && !fixedValue.equals(propertyValue)) {
					match = false;
					break;
				}
			}
			if (match) {
				return rule;
			}
		}
		
		return defaultRule;
	}
	
	//--------------------------------------- private methods ---------------------------------------
	
	private boolean matchClientType(int clientType, int ruleClientType) {
		if (clientType == 0) return true;
		return ClientType.supports(ruleClientType, clientType);
	}
}
