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
package com.bstek.dorado.vidorsupport.rule;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.util.clazz.ClassUtils;
import com.bstek.dorado.view.manager.ViewConfig;

public class RuleSetBuilder extends BuildContext {
	private Log log = LogFactory.getLog(this.getClass());
	
	static RuleSetBuilder create() {
		RuleSetBuilder context =  new RuleSetBuilder();
		
		//基本editor
		context.editorMetas.put(Boolean.class.getName(), new BooleanEditorMeta());
		for (TraditionEditorMeta editorMeta: TraditionEditorMeta.ALL) {
			String name = editorMeta.getName().toLowerCase();
			context.editorMetas.put(name, editorMeta);
		}
		return context;
	}
	
	public Map<com.bstek.dorado.idesupport.model.Rule, Rule> build(com.bstek.dorado.idesupport.model.RuleSet ruleSet) {
		List<com.bstek.dorado.idesupport.model.Rule> ruleList = new ArrayList<com.bstek.dorado.idesupport.model.Rule>(ruleSet.getRuleMap().values());
		List<com.bstek.dorado.idesupport.model.Rule> complexRules = new ArrayList<com.bstek.dorado.idesupport.model.Rule>(50);
	
		Map<com.bstek.dorado.idesupport.model.Rule, Rule> ruleMap = new LinkedHashMap<com.bstek.dorado.idesupport.model.Rule, Rule>(200);
		
		for (com.bstek.dorado.idesupport.model.Rule rule: ruleList) {
			if (!rule.isAbstract()) {
				Rule d7rule = this.create(null, rule, rule.getName());
				ruleMap.put(rule, d7rule);
				
				if(rule.getChildren().size() > 0) {
					complexRules.add(rule);
				} else {
					d7rule.setChildren(new Child[0]);
				}
			}
		}
		
		for (com.bstek.dorado.idesupport.model.Rule d7rule: complexRules) {
			this.putChildren(d7rule, ruleMap);
		}
		
		return ruleMap;
	}
	
	private boolean fixChild(Class<?> beanClass, Child d7Child) {
		if (DataType.class.isAssignableFrom(beanClass)) {
			String propertyName = d7Child.getProperty();
			if ("propertyDef".equals(propertyName)) {
				d7Child.setProperty("propertyDefs");
				d7Child.setMemberAggregated(true);
				return true;
			}
		}
		return false;
	}
	
	private void putChildren(com.bstek.dorado.idesupport.model.Rule rule, 
			Map<com.bstek.dorado.idesupport.model.Rule, Rule> ruleMap) {
		Rule d7rule = ruleMap.get(rule);
		if (d7rule.getChildren() == null) {
			Collection<com.bstek.dorado.idesupport.model.Child> children = rule.getChildren().values();
			//根据规则构造全部对应的Child，但是这些Child并不是所有的都可以被使用
			List<Child> all_d7children = new ArrayList<Child>(children.size());
			for (com.bstek.dorado.idesupport.model.Child child: children) {
				Child d7child = new Child(child);
				all_d7children.add(d7child);
				
				Set<com.bstek.dorado.idesupport.model.Rule> concreteRules = child.getConcreteRules();
				Set<String> memberRuleIDs = new LinkedHashSet<String>(concreteRules.size());
				for (com.bstek.dorado.idesupport.model.Rule concreteRule: concreteRules) {
					Rule d7memberRule = this.put(d7rule, concreteRule, ruleMap);
					String memberId = d7memberRule.getId();
					memberRuleIDs.add(memberId);
				}
				d7child.setMemberRuleIDs(memberRuleIDs.toArray(new String[memberRuleIDs.size()]));
			}
			this.setChildren(d7rule, rule, all_d7children);
		}
	}
	
	private void setChildren(Rule d7rule, com.bstek.dorado.idesupport.model.Rule rule, List<Child> all_d7children) {
		String className = rule.getType();
		if (ViewConfig.class.getName().equals(className)) {
			d7rule.setChildren(all_d7children.toArray(new Child[all_d7children.size()]));
			return;
		}
		
		Class<?> beanClass = null;
		PropertyDescriptor[] propertyDescriptors = null;
		if (className != null && className.length() > 0) {
			try {
				beanClass = ClassUtils.forName(className);
				propertyDescriptors = PropertyUtils.getPropertyDescriptors(beanClass);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		//计算每一个“子规则”是否对应“主规则”的某个属性
		List<Boolean> appendable = new ArrayList<Boolean>(all_d7children.size());
		List<Child> d7Children_Appendable = new ArrayList<Child>(appendable.size());
		List<Child> d7Children_Question = new ArrayList<Child>(appendable.size());
		for (Child d7Child: all_d7children) {
			String childProperty = d7Child.getProperty();
			boolean toAppend = false;
			if (propertyDescriptors != null) {
				boolean found = false;
				for (PropertyDescriptor desc: propertyDescriptors) {
					String descName = desc.getName();
					if (descName.equals(childProperty)) {
						found = true;
						break;
					}
				}
				
				toAppend = found;
			} else {
				toAppend = true;
			}
			
			if (!toAppend) {
				toAppend = this.fixChild(beanClass, d7Child);
			}
			
			if (toAppend) {
				d7Children_Appendable.add(d7Child);
			} else {
				d7Children_Question.add(d7Child);
			}
			appendable.add(toAppend);
		}

		//将“子对象”设置给“主规则”
		if (d7Children_Question.isEmpty()) {
			d7rule.setChildren(all_d7children.toArray(new Child[all_d7children.size()]));
		} else {
			boolean found = false;
			if (d7Children_Appendable.size() == 1) {
				//如果只有一个“子规则”是对应于“主规则”的属性的
				Child child = d7Children_Appendable.get(0);
				String[] memberRuleIDs = child.getMemberRuleIDs();
				Set<String> ruleIDs = new LinkedHashSet<String>(memberRuleIDs.length + 20);
				for (String i: memberRuleIDs) {
					ruleIDs.add(i);
				}
				
				for (Child child_Q: d7Children_Question) {
					for (String i: child_Q.getMemberRuleIDs()) {
						ruleIDs.add(i);
					}
				}
				
				child.setMemberRuleIDs(ruleIDs.toArray(new String[ruleIDs.size()]));
				d7rule.setChildren(new Child[]{child});
				found = true;
			} 
			
			if (!found && d7Children_Appendable.size() == 0) {
				for (PropertyDescriptor desc: propertyDescriptors) {
					Method readMethod = desc.getReadMethod();
					if (readMethod != null) {
						Class<?> returnType = readMethod.getReturnType();
						ClientProperty clientProperty = readMethod.getAnnotation(ClientProperty.class);
						Class<?> targetType = Collection.class;
						if (targetType.isAssignableFrom(returnType)) {
							XmlSubNode xmlSubNode = readMethod.getAnnotation(XmlSubNode.class);
							if (xmlSubNode == null)
								continue;
							
							String descName = desc.getName();
							String propertyName = descName;
							if ((clientProperty != null) && !clientProperty.ignored()) {
								String clientPropertyName = clientProperty.propertyName();
								if (clientPropertyName != null && clientPropertyName.length() > 0) {
									propertyName = clientPropertyName;
								}
							}
							
							Child child = new Child();
							child.setName(descName);
							child.setProperty(propertyName);
							child.setFixed(false);
							child.setAggregated(false);
							child.setWrappered(false);
							child.setMemberAggregated(true);
							Set<String> ruleIDs = new LinkedHashSet<String>(20);
							for (Child child_Q: d7Children_Question) {
								String[] memberRuleIDs = child_Q.getMemberRuleIDs();
								for (String i: memberRuleIDs) {
									ruleIDs.add(i);
								}
							}
							child.setMemberRuleIDs(ruleIDs.toArray(new String[ruleIDs.size()]));
							d7rule.setChildren(new Child[]{child});
							
							found = true;
							break;
						}
					}
				}
			}
			
			if (!found) {
				d7rule.setChildren(all_d7children.toArray(new Child[all_d7children.size()]));
				
				String msg = "Rule: " + d7rule.getId() + ";";
				msg += "\n AllRight_Children:";
				for (int i=0; i<d7Children_Appendable.size(); i++) {
					Child child = d7Children_Appendable.get(i);
					String childName = child.getName();
					String childProperty = child.getProperty();
					String[] memberRuleIDs = child.getMemberRuleIDs();
					msg += "\n  " + (i+1) + ". " + "Name=" + childName + "; Property=" + childProperty + "; ";
					msg += "memberRuleIDs=" + StringUtils.join(memberRuleIDs, ',') + ";";
				}
				msg += "\n Problem:";
				for (int i=0; i<d7Children_Question.size(); i++){
					Child child = d7Children_Question.get(i);
					String childName = child.getName();
					String childProperty = child.getProperty();
					String[] memberRuleIDs = child.getMemberRuleIDs();
					boolean aggregated = child.isAggregated();
					boolean fixed = child.isFixed();
					boolean wrappered = child.isWrappered();
					boolean memberAggregated = child.isMemberAggregated();
					msg += "\n  " + (i+1) + ". " + "Name=" + childName + "; Property=" + childProperty + "; ";
					msg += "Aggregated=" + aggregated + "; Fixed=" + fixed + "; ";
					msg += "Wrappered=" + wrappered + "; MemberAggregated=" + memberAggregated;
					msg += "memberRuleIDs="+StringUtils.join(memberRuleIDs, ',') + ";";
				}
				log.error("XXX -- how to deal with the children?\n" + msg);
			}
		}
		
		//根据每个子规则保存的是否是集合对象，设置memberAggregated属性
		Child[] children = d7rule.getChildren();
		if (propertyDescriptors != null) {
			for (Child d7Child: children) {
				for (PropertyDescriptor desc: propertyDescriptors) {
					Method readMethod = desc.getReadMethod();
					if (readMethod != null) {
						XmlSubNode xmlSubNode = readMethod.getAnnotation(XmlSubNode.class);
						if (xmlSubNode != null) {
							String descName = desc.getName();
							String propertyName = d7Child.getProperty();
							if (descName.equals(propertyName)) {
								Class<?> returnType = readMethod.getReturnType();
								d7Child.setMemberAggregated(Collection.class.isAssignableFrom(returnType));
								break;
							}
						}
					}
				}
			}
		}
		
		//@例如:Container,FloatContainer,HtmlContainer
		if (children.length == 1) {
			Child child = children[0];
			if (child.isMemberAggregated()) {
				child.setAggregated(false);
			}
		}
	}
	
	private Rule put(Rule d7parentRule, com.bstek.dorado.idesupport.model.Rule rule, 
			Map<com.bstek.dorado.idesupport.model.Rule, Rule> ruleMap) {
		Rule d7rule = ruleMap.get(rule);
		if (d7rule == null) {
			d7rule = this.create(d7parentRule, rule, rule.getName());
			ruleMap.put(rule, d7rule);
			
			this.putChildren(rule, ruleMap);
		}
		
		return d7rule;
	}
	
	private Rule create(Rule parent, com.bstek.dorado.idesupport.model.Rule childRule, String name) {
		Rule d7ChildRule = new Rule(childRule, this);
		if (parent != null) {
			d7ChildRule.setId(parent.getId() + "/" + name);
			d7ChildRule.setLevel(parent.getLevel() + 1);
		} else {
			d7ChildRule.setId(name);
			d7ChildRule.setLevel(0);
		}
		
		return d7ChildRule;
	}
}
