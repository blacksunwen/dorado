package com.bstek.dorado.vidorsupport.internal.vidor;

import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.bstek.dorado.vidorsupport.internal.rule.Property;
import com.bstek.dorado.vidorsupport.internal.rule.Rule;
import com.bstek.dorado.vidorsupport.internal.rule.RuleSet;
import com.bstek.dorado.data.JsonUtils;

public class JsonReader {

	private static class ParseContext {
		private RuleSet ruleSet;
		
		ParseContext(RuleSet ruleSet) {
			this.ruleSet = ruleSet;
		}
		public Rule getRule(String ruleId) {
			return ruleSet.getRule(ruleId);
		}
	}
	
	public Document parse(String json, RuleSet ruleSet) throws Exception {
		ParseContext context = new ParseContext(ruleSet);
		Document document = DocumentHelper.createDocument();
		ObjectNode objectNode = (ObjectNode) JsonUtils
				.getObjectMapper().readTree(json);
		
		Element rootElement = this.object2element(objectNode, context);
		document.setRootElement(rootElement);
		return document;
	}
	
	private Element object2element(ObjectNode objectNode, ParseContext context) {
		String ruleId = JsonUtils.getString(objectNode, XmlNode.RULE);
		Rule rule = context.getRule(ruleId);
		String xmlNodeName = rule.getXmlNodeName();
		Element element = DocumentHelper.createElement(xmlNodeName);
		
		this.writeAttributes(element, rule, objectNode, context);
		this.writeLayout(element, rule, objectNode, context);
		this.writePosition(element, rule, objectNode, context);
		this.writeNodes(element, rule, objectNode, context);
		this.writeEvents(element, rule, objectNode, context);
		
		this.writeUnVisibleAttributes(element, rule, objectNode, context);
		this.writeUnVisibleNodes(element, rule, objectNode, context);
		return element;
	}
	
	private void writeLayout(Element element, Rule rule, ObjectNode objectNode, ParseContext context) {
		String fieldName = XmlNode.LAYOUT;
		JsonNode fileldValue = objectNode.get(fieldName);
		if (fileldValue != null) {
			ObjectNode lauoutNode = (ObjectNode)fileldValue;
			LayoutAttribute layout = new LayoutAttribute(lauoutNode);
			String expression = layout.toExpression();
			if (expression != null && expression.length() > 0) {
				element.addAttribute(XmlNode.LAYOUT, expression);
			}
		}
	}
	
	private void writePosition(Element element, Rule rule, ObjectNode objectNode, ParseContext context) {
		String fieldName = PositionAttribute.POSITION;
		JsonNode fileldValue = objectNode.get(fieldName);
		if (fileldValue != null) {
			PositionAttribute position = new PositionAttribute((ObjectNode)fileldValue);
			String expression = position.toExpression();
			if (expression != null && expression.length() > 0) {
				element.addAttribute(XmlNode.LAYOUT_CONSTRAINT, expression);
			}
		}
	}
	
	private void writeUnVisibleAttributes(Element element, Rule rule, ObjectNode objectNode, ParseContext context) {
		//attributes
		ObjectNode attrNode = (ObjectNode)objectNode.get(XmlNode.UV_ATTRS);
		if (attrNode != null) {
			Iterator<String> fieldNameItr = attrNode.getFieldNames();
			while(fieldNameItr.hasNext()) {
				String fieldName = fieldNameItr.next();
				JsonNode fileldValue = attrNode.get(fieldName);
				if (fileldValue.isNull()) {
					continue;
				}
				
				Property property = rule.getProperty(fieldName);
				if (fileldValue.isTextual()) {
					String text = fileldValue.asText();
					if (text != null && text.length() > 0) {
						if (property == null || property.isPrimitive()) {
							element.addAttribute(fieldName, text);
						} else {
							Element propertyElement = element.addElement(PropertyNode.NODE_NAME);
							propertyElement.addAttribute(XmlNode.NAME, fieldName);
							propertyElement.setText(text);
						}
					}
				} 
			}
		}
		
	}
	
	private void writeAttributes(Element element, Rule rule, ObjectNode objectNode, ParseContext context) {
		//fixed properties
		Property[] fixedProperties = rule.getFixedProperties();
		for (Property property: fixedProperties) {
			String name = property.getName();
			String value = property.getDefaultValue();
			if (value != null && value.length() > 0) {
				element.addAttribute(name, value);
			}
		}
		
		//attributes
		ObjectNode attrNode = (ObjectNode)objectNode.get(XmlNode.ATTRS);
		if (attrNode != null) {
			Iterator<String> fieldNameItr = attrNode.getFieldNames();
			while(fieldNameItr.hasNext()) {
				String fieldName = fieldNameItr.next();
				JsonNode fileldValue = attrNode.get(fieldName);
				if (fileldValue.isNull()) {
					continue;
				} 
				
				if (rule.hasFixedProperty(fieldName)) {
					continue;
				}
				
				Property property = rule.getProperty(fieldName);
				if (property == null) {
					throw new IllegalArgumentException("unknown property [" + fieldName + "]");
				}
				
				if (fileldValue.isTextual()) {
					String text = fileldValue.asText();
					if (text != null && text.length() > 0) {
						if (property.isPrimitive()) {
							element.addAttribute(fieldName, text);
						} else {
							Element propertyElement = element.addElement(PropertyNode.NODE_NAME);
							propertyElement.addAttribute(XmlNode.NAME, fieldName);
							propertyElement.setText(text);
						}
					}
				} else if (fileldValue.isObject()) {
					Element propertyElement = element.addElement(PropertyNode.NODE_NAME);
					propertyElement.addAttribute(XmlNode.NAME, fieldName);
					
					Element entityElement = null;
					String propertyType = property.getJavaClass();
					if ("java.lang.Object".equals(propertyType)) {
						entityElement = propertyElement.addElement(EntityNode.NODE_NAME);
					} else {
						entityElement = propertyElement;
					}
					
					if (entityElement == null) {
						throw new IllegalArgumentException("cant not set property [" + fieldName + "] value [" + fileldValue.asText() + "]");
					}
					
					ObjectNode valueNode = (ObjectNode)fileldValue;
					this.entity(valueNode, entityElement);
				} else if (fileldValue.isArray()) {
					Element arrayElement = this.array((ArrayNode)fileldValue);
					Element propertyElement = element.addElement(PropertyNode.NODE_NAME);
					propertyElement.addAttribute(XmlNode.NAME, fieldName);
					propertyElement.add(arrayElement);
				} else {
					throw new JsonException(fileldValue);
				}
			}
		}
	}
	
	private Element array(ArrayNode arrayNode) {
		Element arrayElement = DocumentHelper.createElement(CollectionlNode.NODE_NAME);
		for (int i=0; i<arrayNode.size(); i++) {
			JsonNode valueNode = arrayNode.get(i);
			if (valueNode.isObject()) {
				Element entityElemet = this.entity((ObjectNode)valueNode, null);
				arrayElement.add(entityElemet);
			} else if (valueNode.isTextual()) {
				Element valueElement = this.value((TextNode)valueNode);
				arrayElement.add(valueElement);
			} else {
				throw new JsonException(valueNode);
			}
		}
		
		return arrayElement;
	}
	
	private Element value(TextNode textNode) {
		Element element = DocumentHelper.createElement(ValueNode.NODE_NAME);
		String text = textNode.getTextValue();
		if (text != null && text.length() > 0) {
			element.setText(text);
		}
		
		return element;
	}
	
	private Element entity(ObjectNode objectNode, Element entityElement) {
		if (entityElement == null)
			entityElement = DocumentHelper.createElement(EntityNode.NODE_NAME);
		
		Iterator<String> fieldNameItr = objectNode.getFieldNames();
		while(fieldNameItr.hasNext()) {
			String fieldName = fieldNameItr.next();
			JsonNode fileldValue = objectNode.get(fieldName);
			
			Element propertyElement = entityElement.addElement(PropertyNode.NODE_NAME);
			propertyElement.addAttribute(XmlNode.NAME, fieldName);
			if (fileldValue.isTextual()) {
				String text = fileldValue.asText();
				propertyElement.setText(text);
			} else if (fileldValue.isArray()) {
				Element arrayElement = this.array((ArrayNode)fileldValue);
				propertyElement.add(arrayElement);
			} else {
				throw new JsonException(fileldValue);
			}
		}
		return entityElement;
	}
	
	private void writeNodes(Element parentElement, Rule parentRule, ObjectNode parentNode, ParseContext context) {
		ArrayNode arrayNode = (ArrayNode)parentNode.get(XmlNode.NODES);
		if (arrayNode != null) {
			for (int i=0; i < arrayNode.size(); i++) {
				JsonNode node = arrayNode.get(i);
				if (node.isObject()) {
					ObjectNode oNode = (ObjectNode)node;
					Element element = object2element(oNode, context);
					parentElement.add(element);
				}
			}
		}
	}
	

	private void writeUnVisibleNodes(Element parentElement, Rule parentRule, ObjectNode parentNode, ParseContext context) {
		ObjectNode metaNode = (ObjectNode)parentNode.get(XmlNode.META);
		if (metaNode != null) {
			Element element = parentElement.addElement(PropertyNode.NODE_NAME);
			element.addAttribute(XmlNode.NAME, MetaNode.NAME_VALUE);
			Iterator<String> fieldNameItr = metaNode.getFieldNames();
			while(fieldNameItr.hasNext()) {
				String fieldName = fieldNameItr.next();
				JsonNode fileldValue = metaNode.get(fieldName);
				if (fileldValue.isNull()) {
					continue;
				}
				
				if (fileldValue.isTextual()) {
					String text = fileldValue.asText();
					if (text != null && text.length() > 0) {
						Element propertyElement = element.addElement(PropertyNode.NODE_NAME);
						propertyElement.addAttribute(XmlNode.NAME, fieldName);
						propertyElement.setText(text);
					}
				}
			}
		}
	}
	
	private void writeEvents(Element element, Rule rule, ObjectNode objectNode, ParseContext context) {
		ArrayNode arrayNode = (ArrayNode)objectNode.get(XmlNode.EVTS);
		if (arrayNode != null) {
			for (int i=0; i < arrayNode.size(); i++) {
				JsonNode node = arrayNode.get(i);
				if (node.isObject()) {
					ObjectNode oNode = (ObjectNode)node;
					Iterator<String> fieldNameItr = oNode.getFieldNames();
					Element eventElement = DocumentHelper.createElement(EventNode.NODE_NAME);
					while(fieldNameItr.hasNext()) {
						String fieldName = fieldNameItr.next();
						String fieldValue = oNode.get(fieldName).asText();
						if (EventNode.TEXT.equals(fieldName)) {
							eventElement.setText(fieldValue);
						} else {
							eventElement.addAttribute(fieldName, fieldValue);
						}
					}
					
					String script = eventElement.getText();
					if (script != null && script.length() > 0) {
						element.add(eventElement);
					}
				}
			}
		}
	}
}
