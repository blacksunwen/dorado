package com.bstek.dorado.vidorsupport.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.vidorsupport.iapi.IDataTypeWorkshop;
import com.bstek.dorado.vidorsupport.iapi.IRuleSetFactory;
import com.bstek.dorado.vidorsupport.internal.output.OutputContext;
import com.bstek.dorado.vidorsupport.internal.rule.RuleSet;
import com.bstek.dorado.vidorsupport.internal.vidor.XmlNode;
import com.bstek.dorado.vidorsupport.internal.vidor.XmlReader;

public abstract class AbstractDataTypeWorkshop implements IDataTypeWorkshop {
	private IRuleSetFactory ruleSetFactory;
	
	private List<String> baseTypeNames;
	
	private String baseTypeLocation;
	private XmlDocumentBuilder xmlDocumentBuilder;
	
	public void setRuleSetFactory(IRuleSetFactory ruleSetFactory) {
		this.ruleSetFactory = ruleSetFactory;
	}
	public IRuleSetFactory getRuleSetFactory() {
		return ruleSetFactory;
	}

	public String getBaseTypeLocation() {
		return baseTypeLocation;
	}
	public void setBaseTypeLocation(String baseTypeLocation) throws Exception {
		this.baseTypeLocation = baseTypeLocation;
	}
	public XmlDocumentBuilder getXmlDocumentBuilder() {
		return xmlDocumentBuilder;
	}
	public void setXmlDocumentBuilder(XmlDocumentBuilder xmlDocumentBuilder) {
		this.xmlDocumentBuilder = xmlDocumentBuilder;
	}
	
	@Override
	public Collection<String> baseNames() throws Exception {
		if (baseTypeNames == null) {
			synchronized (this.getClass()) {
				if (baseTypeNames == null) {
					Resource resource = ResourceUtils.getResource(baseTypeLocation);
					baseTypeNames = this.loadDataTypes(resource);
				}
			}
		}
		return baseTypeNames;
	}

	protected List<String> loadDataTypes(Resource resource) throws Exception {
		Document document = xmlDocumentBuilder.loadDocument(resource, "UTF-8");
		Element documentElement = document.getDocumentElement();
		List<Element> dataTypeElements = DomUtils.getChildElements(documentElement);
		List<String> names = new ArrayList<String>(dataTypeElements.size());
		for (Element element: dataTypeElements) {
			String name = element.getAttribute("name");
			names.add(name);
		}
		return names;
	}
	
	protected boolean isGlobal(String name) {
		return name.startsWith(GLOBAL_PREFIX);
	}
	
	protected String dataTypeName(String name) {
		if (this.isGlobal(name)) {
			String n = name.substring(GLOBAL_PREFIX.length());
			return n;
		} else {
			return name;
		}
	}
	
	protected String json(URL modelUrl, String name, String ruleID) throws Exception {
		RuleSet ruleSet = this.getRuleSetFactory().get();
		XmlReader xmlReader = new XmlReader();
		XmlReader.ParseParameter parseParameter = new XmlReader.ParseParameter();
		parseParameter.setRuleSet(ruleSet);
		XmlNode xmlNode = xmlReader.parse(modelUrl, parseParameter);
		
		List<XmlNode> nodes = xmlNode.getNodes();
		XmlNode dataTypeNode = null;
		for (XmlNode node: nodes) {
			String nodeName = node.getAttributes().get("name");
			if (name.equals(nodeName) && ruleID.equals(node.getRuleId())) {
				dataTypeNode = node;
				break;
			}
		}
		
		if (dataTypeNode != null) {
			OutputContext outputContext = new OutputContext();
			dataTypeNode.output(outputContext);
			String json = outputContext.getWriter().toString();
			return json;
		}
		return null;
	}
	
	protected String getAttributeValue(JsonNode node, String attributeName) {
		String value = null;
		JsonNode attrsNode = node.get("attrs");
		if (attrsNode != null) {
			JsonNode nameNode = attrsNode.get(attributeName);
			if (nameNode != null) {
				value = nameNode.asText();
			}
		}
		
		return value;
	}
	
	protected void doMerge(ObjectNode to, ObjectNode from) throws Exception {
		//检查form是否存在nodes节点
		ArrayNode nodes_from = (ArrayNode)from.get("nodes");
		if (nodes_from == null || nodes_from.size() == 0) return;
		
		//保证to具有nodes节点
		ArrayNode nodes_to = (ArrayNode)to.get("nodes");
		if (nodes_to == null) {
			nodes_to = to.arrayNode();
			to.put("nodes", nodes_to);
		}
		
		//to的子节点name的集合
		Set<String> names_to = new HashSet<String>();
		for (int i=0; i<nodes_to.size(); i++) {
			JsonNode aNode = nodes_to.get(i);
			String name = this.getAttributeValue(aNode, "name");
			if (name != null && name.length() > 0) {
				names_to.add(name);
			}
		}
		
		//from的子节点合并到to的子节点集合
		for (int i=0, j=nodes_from.size(); i<j; i++) {
			JsonNode node_from = nodes_from.get(i);
			String name = this.getAttributeValue(node_from, "name");
			if (name != null && name.length() > 0) {
				if (!names_to.contains(name)) {
					nodes_to.add(node_from);
					names_to.add(name);
				}
			}
		}
	}
	
	protected ObjectNode addPropertyNode(ArrayNode nodes, String name, String dataTypeName) throws Exception {
		ObjectNode node = nodes.addObject();
		node.put("rule", "BasePropertyDef");
		ObjectNode attrsNode = node.putObject("attrs");
		attrsNode.put("name", name);
		if (dataTypeName != null) {
			attrsNode.put("dataType", dataTypeName);
		}
		return node;
	}
	
	protected Set<String> getParentNames(ObjectNode dataTypeNode) {
		Set<String> parentNames = new LinkedHashSet<String>();
		String parentNameString = this.getAttributeValue(dataTypeNode, "parent");
		if (parentNameString != null) {
			String[] ns = StringUtils.split(parentNameString, ',');
			for (String n: ns) {
				parentNames.add(n);
			}
		}
		
		return parentNames;
	}
	
	@Override
	public String json(Map<String, String> jsonMap, String targetName)
			throws Exception {
		Map<String, ObjectNode> jsonObjectMap = new HashMap<String, ObjectNode>(jsonMap.size());
		String targetStr = jsonMap.get(targetName);
		if (targetStr == null) 
			throw new Exception("No dataType definition be found.[" + targetName + "]");
		
		ObjectNode dataTypeNode = (ObjectNode)JsonUtils.getObjectMapper().readTree(targetStr);
		
		Set<String> parentNames = this.getParentNames(dataTypeNode);
		for (String parentName: parentNames) {
			ObjectNode parentNode = null;
			if (this.isGlobal(parentName)) {
				parentName = this.dataTypeName(parentName);
				parentNode = this.getFromGlobal(parentName, null);
			} else {
				if (jsonMap.containsKey(parentName)) {
					String parentJson = jsonMap.get(parentName);
					parentNode = (ObjectNode)JsonUtils.getObjectMapper().readTree(parentJson);
					this.doMergeFromGlobal(parentNode, jsonObjectMap);
				} else {
					parentNode = this.getFromGlobal(parentName, null);
				}
			}
			
			jsonObjectMap.put(parentName, parentNode);
		}
		
		this.doMergeFromGlobal(dataTypeNode, jsonObjectMap);
		return dataTypeNode.toString();
	}
	
	abstract
	protected ObjectNode getFromGlobal(String name, Map<String, ObjectNode> jsonObjectMap) throws Exception;
	
	protected void doMergeFromGlobal(ObjectNode dataTypeNode, Map<String, ObjectNode> jsonObjectMap) throws Exception {
		//与parent合并
		Set<String> parentNames = this.getParentNames(dataTypeNode);
		for (String parentName: parentNames) {
			parentName = this.dataTypeName(parentName);
			ObjectNode parent = jsonObjectMap.get(parentName);
			if (parent == null) {
				parent = this.getFromGlobal(parentName, jsonObjectMap);
				if (parent == null) {
					throw new Exception("No dataType definition be found.[" + parentName + "]");
				} else {
					jsonObjectMap.put(parentName, parent);
				}
			}
			
			this.doMerge(dataTypeNode, parent);
		}
	}
}
