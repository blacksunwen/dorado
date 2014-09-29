package com.bstek.dorado.vidorsupport.vidor;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.bstek.dorado.vidorsupport.exception.XmlException;
import com.bstek.dorado.vidorsupport.rule.Rule;
import com.bstek.dorado.vidorsupport.rule.RuleSet;
import com.bstek.dorado.common.ClientType;

public class XmlReader {
	
	public static class ParseParameter {
		private RuleSet ruleSet;
		private String clientType = ClientType.DESKTOP_NAME;
		private String ruleId;//解析时xml根节点的ruleId
		
		public RuleSet getRuleSet() {
			return ruleSet;
		}
		public void setRuleSet(RuleSet ruleSet) {
			this.ruleSet = ruleSet;
		}
		public String getClientType() {
			return clientType;
		}
		public void setClientType(String clientType) {
			if (clientType == null || clientType.length() ==0)
				throw new IllegalArgumentException("clientType must not be empty.");
			this.clientType = clientType;
		}
		public String getRuleId() {
			return ruleId;
		}
		public void setRuleId(String ruleId) {
			this.ruleId = ruleId;
		}
		
		public boolean supportsClientTypes(int targetType) {
			if (targetType == 0) return true;
			
			int types = ClientType.parseClientTypes(this.getClientType());
			return ClientType.supports(types, targetType);
		}
	}
	
	public XmlNode parse(URL url, ParseParameter parameter) throws Exception {
		SAXReader saxReader = this.createSAXReader();
	    Document document = saxReader.read(url);
	    
		XmlNode result = this.parse(document, parameter);
		return result;
	}
	
	public XmlNode parse(InputStream input, ParseParameter parameter) throws Exception {
		SAXReader saxReader = this.createSAXReader();
	    Document document = saxReader.read(input);
	    
		XmlNode result = this.parse(document, parameter);
		return result;
	}
	
	public XmlNode parse(Reader reader, ParseParameter parameter) throws Exception {
		SAXReader saxReader = this.createSAXReader();
	    Document document = saxReader.read(reader);
	    
		XmlNode result = this.parse(document, parameter);
		return result;
	}
	
	protected SAXReader createSAXReader() {
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding("UTF-8");
		return saxReader;
	}
	
	public XmlNode parse(Document document, ParseParameter parameter) throws Exception {
		Element rootElement = document.getRootElement();
		String ruleId = parameter.getRuleId();
		if (ruleId == null || ruleId.length() == 0) {
			ruleId = rootElement.getName();
		}
		Rule rule = parameter.getRuleSet().getRule(ruleId);
		XmlNode result = this.create(rootElement, rule, parameter);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private XmlNode create(Element element, Rule rule, ParseParameter parameter) {
		XmlNode xmlNode = rule==null? this.newNodeByXml(element): this.newNodeByRule(element, rule);
		if (xmlNode == null) 
			throw new XmlException(element);
		//处理属性
		xmlNode.acceptAttributes(element, rule);
		//处理子节点
		List<Element> innerElements = element.elements();
		if (!innerElements.isEmpty()) {
			for (Element innerElement: innerElements) {
				XmlNode innerNode = this.newNodeByXml(innerElement);
				if (innerNode != null) {
					innerNode = this.create(innerElement, null, parameter);
					innerNode.appendTo(xmlNode, innerElement);
				} else {
					int clientType = ClientType.parseClientTypes(parameter.getClientType());
					RuleSet ruleSet = parameter.getRuleSet();
					Rule innerRule = ruleSet.findRule(rule, innerElement, clientType);
					innerNode = this.create(innerElement, innerRule, parameter);
					innerNode.appendTo(xmlNode, innerElement);
				}
			}
		}
		
		return xmlNode;
	}
	
	//创建通用的节点，这种节点只与XML节点的name有关，与rule无关
	private XmlNode newNodeByXml(Element element) {
		String xmlNodeName = element.getName();
		XmlNode xmlNode = null;
		if (PropertyNode.NODE_NAME.equals(xmlNodeName)) {
			String nameValue = element.attributeValue("name");
			if (MetaNode.NAME_VALUE.equals(nameValue)) {
				xmlNode = new MetaNode();
			} else {
				xmlNode = new PropertyNode();
			}
		} else 
		if (ValueNode.NODE_NAME.equals(xmlNodeName)) {
			xmlNode = new ValueNode();
		} else if (EntityNode.NODE_NAME.equals(xmlNodeName)) {
			xmlNode = new EntityNode();
		} else if (CollectionlNode.NODE_NAME.equals(xmlNodeName)) {
			xmlNode = new CollectionlNode();
		} else if (EventNode.NODE_NAME.equals(xmlNodeName)) {
			xmlNode = new EventNode();
		}
		
		return xmlNode;
	}
	
	//创建与规则相关的节点
	private XmlNode newNodeByRule(Element element, Rule rule) {
		String ruleId = rule.getId();
		XmlNode xmlNode = new XmlNode(ruleId);
		return xmlNode;
	}
}
