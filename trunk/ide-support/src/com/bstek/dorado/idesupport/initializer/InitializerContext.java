package com.bstek.dorado.idesupport.initializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.collections.keyvalue.MultiKey;

import com.bstek.dorado.config.xml.TypeAnnotationInfo;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.template.ChildTemplate;
import com.bstek.dorado.view.output.Outputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-26
 */
public class InitializerContext {
	private RuleTemplateManager ruleTemplateManager;
	private Stack<Class<?>> typeStack = new Stack<Class<?>>();
	private Stack<Outputter> outputterStack = new Stack<Outputter>();
	private Stack<String> propertyStack = new Stack<String>();
	private Map<Class<?>, TypeAnnotationInfo> typeAnnotationInfoMap = new HashMap<Class<?>, TypeAnnotationInfo>();
	private Map<MultiKey, ChildTemplate> childTemplateMap = new HashMap<MultiKey, ChildTemplate>();

	public InitializerContext(RuleTemplateManager ruleTemplateManager) {
		this.ruleTemplateManager = ruleTemplateManager;
	}

	public RuleTemplateManager getRuleTemplateManager() {
		return ruleTemplateManager;
	}

	public void pushType(Class<?> type) {
		typeStack.push(type);
	}

	public Class<?> popType() {
		return typeStack.pop();
	}

	public Class<?> getCurrentType() {
		if (typeStack.isEmpty()) {
			return null;
		}
		else {
			return typeStack.peek();
		}
	}

	public void pushOutputter(Outputter outputter) {
		outputterStack.push(outputter);
	}

	public Outputter popOutputter() {
		return outputterStack.pop();
	}

	public Outputter getCurrentOutputter() {
		if (outputterStack.isEmpty()) {
			return null;
		}
		else {
			return outputterStack.peek();
		}
	}

	public void pushProperty(String type) {
		propertyStack.push(type);
	}

	public String popProperty() {
		return propertyStack.pop();
	}

	public String getCurrentProperty() {
		if (propertyStack.isEmpty()) {
			return null;
		}
		else {
			return propertyStack.peek();
		}
	}

	public Map<Class<?>, TypeAnnotationInfo> getTypeAnnotationInfoMap() {
		return typeAnnotationInfoMap;
	}

	public Map<MultiKey, ChildTemplate> getChildTemplateMap() {
		return childTemplateMap;
	}
}
